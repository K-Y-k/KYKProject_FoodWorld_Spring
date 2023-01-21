package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.UploadFile;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * 서비스를 스프링 JPA 구현체로 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 글 등록
     */
    @Override
    public Long upload(Long memberId, BoardUploadDto boardDto, MultipartFile file) throws IOException {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("글 등록 실패: 로그인 상태가 아닙니다." + memberId));

        String fileName = null;
        String filePath = null;

        if (file != null && !file.isEmpty()) {
            log.info("파일 가져와짐");
            
            // 파일경로 지정
//            String fullPath = "C:\\Users\\KOR\\IdeaProjects\\file";
            String fullPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  // System.getProperty("user.dir")는 resource->static->files로 경로를 정했기에 현재 프로젝트의 경로로 담아줌

            // 파일에 이름을 붙일 랜덤으로 식별자 지정
            UUID uuid = UUID.randomUUID();
            fileName = uuid + "_" + file.getOriginalFilename();

            // 파일경로와 파일이름 지정한 객체 생성 및 저장
            File saveFile = new File(fullPath, fileName);
            file.transferTo(saveFile);

            filePath = "/files/" + fileName;
        }



        Board board = boardDto.toEntity(findMember, fileName, filePath);

        log.info("uploadBoard={}", board);
        Board uploadBoard = boardRepository.save(board);

        return uploadBoard.getId();
    }

//    @Override
//    public void upload(Board board, MultipartFile file) throws IOException {
//        if(!file.isEmpty()) {
//            // 파일경로 지정
//            String fullPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  // System.getProperty("user.dir")는 resource->static->files로 경로를 정했기에 현재 프로젝트의 경로로 담아줌
//
//            // 파일에 이름을 붙일 랜덤으로 식별자 지정
//            UUID uuid = UUID.randomUUID();
//            String fileName = uuid + "_" + file.getOriginalFilename();
//
//            // 파일경로와 파일이름 지정한 객체 생성 및 저장
//            File saveFile = new File(fullPath, fileName);
//            file.transferTo(saveFile);
//
//            // DB에 저장
//            board.setFileName(fileName);
//            board.setFilePath("/files/" + fileName);
//
//        }
////        boardRepository.save(board);
//        return;
//    }


    /**
     * 여러 개의 이미지 업로드
     */
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        // ArrayList로 저장 결과 변수 선언
        List<UploadFile> storeFileResult = new ArrayList<>();

        // 루프를 돌려 파일을 모두 찾고 반환
        for (MultipartFile multipartFile : multipartFiles) {
            // 파일이 비어있지 않으면
            if (!multipartFile.isEmpty()) {
                // storeFile호출해서 multipartFile를 넣어준 uploadFile을 리스트에 담아줘야한다.
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public String getFullPath(String filename) {
        return System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files" + filename;
    }

    /**
     * 하나의 파일 저장 MultipartFile을 받아와 UploadFile로 변환
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

        // 비어있으면 널로 반환
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 비어있지 않으면
        // 사용자가 업로드한 파일명 가져오고 ex) image.png
        String originalFilename = multipartFile.getOriginalFilename();

//        // 확장자를 가져오기 : 나중에 서버에서 구분하기 쉽게하려고 ex) png
//        String ext = extractedExt(originalFilename);
//
//        // 서버에 저장하는 파일명 = 유효Id로 ex) "qwe-qwe-123-qwe-qqwe.png"
//        String uuid = UUID.randomUUID().toString();
//
//        // 최종적으로 저장할 파일명
//        String storeFileName = uuid + "." + ext;

        // 위 작업을 한 메서드를 적용한 것
        String storeFileName = createStoreFileName(originalFilename);

        // 파일 경로명으로 변환 및 UploadFile 객체로 반환
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);

    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractedExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractedExt(String originalFilename) {
        // 마지막 .뒤의 위치를 가져온다.
        int pos = originalFilename.lastIndexOf(".");
        // 가져온 위치를 잘라서 확장자를 가져온다.
//        String ext = originalFilename.substring(pos + 1);
//        return ext;
        return originalFilename.substring(pos + 1);
    }

    /**
     * 글 수정
     */
    @Override
    public Long updateBoard(Long boardId, BoardUpdateDto updateParam) {
        Board findBoard = findById(boardId).orElseThrow();

        findBoard.updateBoard(updateParam.getTitle(), updateParam.getContent());

        log.info("수정완료");
        return findBoard.getId();
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    public Page<Board> pageList(Pageable pageable) {
        return boardRepository.pageList(pageable);
    }

    @Override
    public Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable) {
        log.info("제목 검색");

        return boardRepository.findByTitleContaining(titleSearchKeyword, pageable);
    }

    @Override
    public Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable) {
        log.info("작가 검색");

        return boardRepository.findByWriterContaining(writerSearchKeyword, pageable);
    }

    @Override
    public Page<Board> findByTitleContainingAndWriterContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable) {
        log.info("작가, 제목 동시 검색");

        return boardRepository.findByTitleContainingAndWriterContaining(titleSearchKeyword, writerSearchKeyword, pageable);
    }

    @Override
    public void delete(Long boardId) {
        log.info("삭제 완료");

        boardRepository.delete(boardId);
    }

    @Override
    public int updateCount(Long boardId) {
        log.info("조회수 증가");

        return boardRepository.updateCount(boardId);
    }


    public void updateLikeCount(Long boardId, int likeCount) {
        Board findBoard = findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("좋아요 업데이트 실패: 게시글을 찾지 못했습니다." + boardId));

        findBoard.updateLikeCount(likeCount);

        log.info("좋아요 최신화 완료");
    }

}
