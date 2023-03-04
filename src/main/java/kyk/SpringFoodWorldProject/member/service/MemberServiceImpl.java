package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.board.domain.dto.MucstarUploadForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.member.domain.dto.JoinForm;
import kyk.SpringFoodWorldProject.member.domain.dto.UpdateForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.member.repository.ProfileFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ProfileFileRepository profileFileRepository;


    @Value("${file.profileLocation}")
    private String profileLocation;

    /**
     * 회원가입
     */
    @Override
    public Long join(JoinForm form) {
        Member memberEntity = form.toEntity();

        // 중복 회원 검증 로직
        validateDuplicateMember(memberEntity);

        Member savedMember = memberRepository.save(memberEntity);
        return savedMember.getId();
    }

    private void validateDuplicateMember(Member memberEntity) {
        // 문제가 있으면 EXCEPTION
        // 같은 로그인 아이디가 있는지 찾음
        // 반환형태가 Optional<Member>이므로 이렇게 예와처리 가능
        memberRepository.findByLoginId(memberEntity.getLoginId())
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
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
                .orElse(null);
    }


    /**
     * 전체회원조회
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
    public Long changeProfile(Long memberId, UpdateForm form) throws IOException {
        Member member = memberRepository.findById(memberId).get();
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

    private void profileImageUpload(UpdateForm form, Member member) throws IOException {
        String originalFilename = form.getProfileImage().getOriginalFilename();

        // 파일에 이름을 붙일 랜덤으로 식별자 지정
        UUID uuid = UUID.randomUUID();
        String storedFileName = uuid + "_" + originalFilename;
        String savePath = profileLocation;

        // 실제 파일 저장 경로와 파일 이름 지정한 File 객체 생성 및 저장
        form.getProfileImage().transferTo(new File(savePath, storedFileName));

        String attachedType = "yes";

        // DB에 파일 관련 필드 값 저장
        ProfileFile profileFileEntity = ProfileFile.toProfileFileEntity(member, originalFilename, storedFileName, attachedType);
        profileFileRepository.save(profileFileEntity);
    }


}


