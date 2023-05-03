package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.repository.BoardFileRepository;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.member.domain.dto.JoinForm;
import kyk.SpringFoodWorldProject.member.domain.dto.UpdateForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import kyk.SpringFoodWorldProject.exception.DuplicatedMemberLoginIdException;
import kyk.SpringFoodWorldProject.exception.DuplicatedMemberNameException;
import kyk.SpringFoodWorldProject.exception.MemberNotFoundException;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.repository.MenuRecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final MenuRecommendRepository menuRecommendRepository;

    @Value("${file.profileLocation}")
    private String profileLocation;

    @Value("${file.imageFileLocation}")
    private String imageFileLocation;

    @Value("${file.attachFileLocation}")
    private String attachFileLocation;

    @Value("${file.menuRecommendLocation}")
    private String menuRecommendLocation;

    /**
     * 회원가입
     */
    @Override
    public Long join(JoinForm form) {
        Member memberEntity = form.toEntity();

        // 닉네임/아이디 중복 회원 검증 로직
        validateDuplicateMember(memberEntity);

        Member savedMember = memberRepository.saveMember(memberEntity);

        // 파일에 이름을 붙일 랜덤으로 식별자 지정 및 프로필 엔티티 생성
        UUID uuid = UUID.randomUUID();
        String storedFileName = uuid + "_user_icon.PNG";

        ProfileFile profileFile = ProfileFile.builder()
                .originalFileName("user_icon.PNG")
                .storedFileName(storedFileName)
                .member(memberEntity)
                .build();

        // 원본 저장 경로
        Path originalSourcePath = Paths.get(profileLocation + "\\user_icon.PNG");

        // 복사 경로 생성
        Path copySavePath = Paths.get(profileLocation + "\\" + storedFileName);
        
        // 원본 저장 파일을 복사
        try {
            Files.copy(originalSourcePath, copySavePath);
            System.out.println("파일 복사 성공");
        } catch (IOException e) {
            System.out.println("파일 복사 실패: " + e.getMessage());
        }

        // DB에서도 저장
        memberRepository.saveProfile(profileFile);
        return savedMember.getId();
    }

    private void validateDuplicateMember(Member memberEntity) {
        // 문제가 있으면 EXCEPTION
        // 같은 로그인 아이디가 있는지 찾음
        // 반환형태가 Optional<Member>이므로 이렇게 예와처리 가능
        memberRepository.findByName(memberEntity.getName())
                .ifPresent(m-> {
                    throw new DuplicatedMemberNameException("이미 존재하는 닉네임입니다.");
                });

        memberRepository.findByLoginId(memberEntity.getLoginId())
                .ifPresent(m-> {
                    throw new DuplicatedMemberLoginIdException("이미 존재하는 아이디입니다.");
                });
    }

    /**
     * 로그인 기능
     */
    @Override
    public Member login(String loginId, String password) {        // 로그인 실패 시 null로 반환
        // 람다를 활용하여 위 코드를 축약한 것
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElseThrow(() -> new MemberNotFoundException("아이디 또는 패스워드가 일치하지 않습니다."));
    }


    /**
     * 전체 회원 조회
     */
    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    /**
     * 회원 찾기
     */
    @Override
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }


    /**
     * 회원 수정
     */
    @Override
    public Long changeProfile(Long memberId, UpdateForm form) throws IOException {
        Member member = memberRepository.findById(memberId).get();
        
        // 현재 회원의 기존 프로필 사진을 찾고 실제 저장되었던 디렉토리의 프로필 사진을 삭제
        ProfileFile findMemberProfile = memberRepository.findProfileByMember(member);
        Path beforeAttachPath = Paths.get(profileLocation + "\\" + findMemberProfile.getStoredFileName());
        try {
            Files.deleteIfExists(beforeAttachPath);
        } catch (DirectoryNotEmptyException e) {
            log.info("디렉토리가 비어있지 않습니다");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 이번에 새로 수정한 프로필 사진을 실제 디렉토리에 저장하고 DB의 프로필 사진 경로 업데이트
        MultipartFile imageFile = form.getProfileImage();
        if (imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isBlank()) {
            member.changeProfile(form.getName(), form.getLoginId(), form.getPassword(), form.getIntroduce());
            profileImageUpload(form, member);
        } else {
            member.changeProfile(form.getName(), form.getLoginId(), form.getPassword(), form.getIntroduce());
        }

        log.info("회원 프로필 변경");
        return member.getId();
    }

    @Override
    public Page<Member> findPageBy(Pageable pageable) {
        return memberRepository.findPageBy(pageable);
    }

    @Override
    public Page<Member> findByNameContaining(String memberSearchKeyword, Pageable pageable) {
        return memberRepository.findByNameContaining(memberSearchKeyword, pageable);
    }

    /**
     * 회원 탈퇴 및 추방 : 현재 회원과 관련된 물리 실제 파일들도 같이 삭제처리를 해야한다!
     */
    @Override
    public void delete(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("회원 조회 실패: " + memberId));

        // 현재 회원의 기존 프로필 사진을 찾고 실제 저장되었던 디렉토리의 프로필 사진을 삭제
        ProfileFile findMemberProfile = memberRepository.findProfileByMember(findMember);
        Path beforeProfilePath = Paths.get(profileLocation + "\\" + findMemberProfile.getStoredFileName());
        try {
            Files.deleteIfExists(beforeProfilePath);
        } catch (DirectoryNotEmptyException e) {
            log.info("디렉토리가 비어있지 않습니다");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 현재 회원이 작성했던 게시글을 찾고
        List<Board> byMemberBoardList = boardRepository.findByMemberId(memberId);

        // 각 게시글마다 실제 저장되었던 디렉토리의 파일을 삭제
        for (Board board : byMemberBoardList) {
            List<BoardFile> findBoardFiles = boardFileRepository.findByBoard(board);

            // 자유게시판과 추천게시판은 첨부 파일도 같이 넣을 수 있어 따로 첨부파일 + 이미지파일 삭제
            if (board.getBoardType().equals("자유게시판") || board.getBoardType().equals("추천게시판")) {
                for (BoardFile boardFile : findBoardFiles) {
                    if (boardFile.getAttachedType().equals("attached")) {
                        Path beforeAttachPath = Paths.get(attachFileLocation + "\\" + boardFile.getStoredFileName());
                        try {
                            Files.deleteIfExists(beforeAttachPath);
                        } catch (DirectoryNotEmptyException e) {
                            log.info("디렉토리가 비어있지 않습니다");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Path beforeImageFilePath = Paths.get(imageFileLocation + "\\" + boardFile.getStoredFileName());
                        try {
                            Files.deleteIfExists(beforeImageFilePath);
                        } catch (DirectoryNotEmptyException e) {
                            log.info("디렉토리가 비어있지 않습니다");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else { // 먹스타그램은 이미지 파일만 삭제
                for (BoardFile boardFile : findBoardFiles) {
                    Path beforeImageFilePath = Paths.get(imageFileLocation + "\\" + boardFile.getStoredFileName());
                    try {
                        Files.deleteIfExists(beforeImageFilePath);
                    } catch (DirectoryNotEmptyException e) {
                        log.info("디렉토리가 비어있지 않습니다");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        // 메뉴 랜덤 추천 파일도 삭제 처리
        Page<MenuRecommend> findMenus = menuRecommendRepository.findByMemberId(null, memberId);
        for (MenuRecommend findMenu : findMenus) {
            Path beforeMenuFilePath = Paths.get(menuRecommendLocation+"\\"+findMenu.getStoredFileName());
            try {
                Files.deleteIfExists(beforeMenuFilePath);
            } catch (DirectoryNotEmptyException e) {
                log.info("디렉토리가 비어있지 않습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // DB 삭제처리 : CASCADE 설정으로 회원 엔티티가 삭제되면 연관 매핑된 엔티티는 모두 삭제된다.
        memberRepository.delete(memberId);
    }

    private void profileImageUpload(UpdateForm form, Member member) throws IOException {
        String originalFileName = form.getProfileImage().getOriginalFilename();

        // 파일에 이름을 붙일 랜덤으로 식별자 지정
        UUID uuid = UUID.randomUUID();
        String storedFileName = uuid + "_" + originalFileName;
        String savePath = profileLocation;

        // 실제 파일 저장 경로와 파일 이름 지정한 File 객체 생성 및 저장
        form.getProfileImage().transferTo(new File(savePath, storedFileName));

        // 회원의 기존 프로필 사진에서 교체
        memberRepository.updateProfileImage(originalFileName, storedFileName, member.getId());
    }


    /**
     * 회원 프로필 사진 경로 가져오기
     */
    public String findProfileLocation(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("회원 조회 실패: " + memberId));

        ProfileFile findMemberProfile = memberRepository.findProfileByMember(findMember);

        return findMemberProfile.getStoredFileName();
    }

}


