package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.repository.BoardFileRepository;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
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
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final MemberRepository memberRepository;

    @Value("${file.location}")
    private String fileLocation;

    /**
     * 글 등록
     *
     * @return
     */
    @Override
    public Long upload(Long memberId, BoardUploadDto boardDto) throws IOException {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("글 등록 실패: 로그인 상태가 아닙니다." + memberId));

        String fileName = null;
        String filePath = null;

        if (boardDto.getImageFiles() != null && !boardDto.getImageFiles().isEmpty()) {
            log.info("파일 가져와짐");

            MultipartFile imageFiles = boardDto.getImageFiles();
            String originalFilename = imageFiles.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String storedFileName = uuid + "_" + originalFilename;
            String savePath = fileLocation;
            imageFiles.transferTo(new File(savePath, storedFileName));

            filePath = "/C:/Users/KOR/IdeaProjects/file/" + storedFileName;

            Board board = boardDto.toSaveFileEntity(findMember, boardDto);
            Long savedId = boardRepository.save(board).getId();


            BoardFile boardFile = BoardFile.toBoardFileEntity(board, originalFilename, storedFileName);

            boardFileRepository.save(boardFile);

            return savedId;

//            // 실제 파일 저장하는 경로 지정
//            String savePath = fileLocation;
////            filePath= System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  // System.getProperty("user.dir")는 resource->static->files로 경로를 정했기에 현재 프로젝트의 경로로 담아줌
//
//            // 파일에 이름을 붙일 랜덤으로 식별자 지정
//            UUID uuid = UUID.randomUUID();
//            fileName = uuid + "_" + boardDto.getImageFiles().getOriginalFilename();
//
//            // 실제 파일 저장 경로와 파일 이름 지정한 File 객체 생성 및 저장
//            boardDto.getImageFiles().transferTo(new File(savePath, fileName));
//
//            // DB에 저장할 경로
//            filePath = "/C:/Users/KOR/IdeaProjects/file/" + fileName;
        } else {

//        Board board = boardDto.toEntity(findMember, fileName, filePath);

            Board board = boardDto.toSaveEntity(findMember, boardDto);
            log.info("uploadBoard={}", board);
            Board uploadBoard = boardRepository.save(board);
            return uploadBoard.getId();
        }
    }

//    public String getFullPath(String fileName) {
//        filePath = "C:\\Users\\KOR\\IdeaProjects\\file\\";
//        return filePath + fileName;
//    }

    //    @Override
//    public Long upload(Long memberId, BoardUploadDto boardDto, MultipartFile file) throws IOException {
//        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
//                new IllegalArgumentException("글 등록 실패: 로그인 상태가 아닙니다." + memberId));
//
//        filePath = null;
//        String fileName = null;
//
//        if (file != null && !file.isEmpty()) {
//            log.info("파일 가져와짐");
//
//            // 파일경로 지정
////            String fullPath = "C:\\Users\\KOR\\IdeaProjects\\file";
////            String fullPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";  // System.getProperty("user.dir")는 현재 프로젝트의 경로로 담아줌
//
////            // 파일에 이름을 붙일 랜덤으로 식별자 지정
////            String uuid = UUID.randomUUID().toString();
////            fileName = uuid + "_" + file.getOriginalFilename();
//
//            String originalFilename = file.getOriginalFilename();
//            fileName = createStoreFileName(originalFilename);
//
//            // 파일경로와 파일이름 지정한 객체 생성 및 저장
//            file.transferTo(new File(getFullPath(fileName)));
//            log.info("file = {}", file.getOriginalFilename());
//
//        }
//
//        Board board = boardDto.toEntity(findMember, fileName, filePath);
//
//        log.info("uploadBoard={}", board);
//        Board uploadBoard = boardRepository.save(board);
//
//        return uploadBoard.getId();
//    }
    private String createStoreFileName(String originalFilename) {
        String ext = extractedExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;

    }

    private String extractedExt(String originalFilename) {
        // 마지막 .뒤의 위치를 가져온다.
        int pos = originalFilename.lastIndexOf(".");
        // 위를 활용하여 확장자를 가져온다.
        return originalFilename.substring(pos + 1);
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


//    /**
//     * 여러 개의 이미지 업로드
//     */
//    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
//        // ArrayList로 저장 결과 변수 선언
//        List<UploadFile> storeFileResult = new ArrayList<>();
//
//        // 루프를 돌려 파일을 모두 찾고 반환
//        for (MultipartFile multipartFile : multipartFiles) {
//            // 파일이 비어있지 않으면
//            if (!multipartFile.isEmpty()) {
//                // storeFile호출해서 multipartFile를 넣어준 uploadFile을 리스트에 담아줘야한다.
//                storeFileResult.add(storeFile(multipartFile));
//            }
//        }
//        return storeFileResult;
//    }
//
//    public String getFullPath(String filename) {
//        return System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files" + filename;
//    }
//
//    /**
//     * 하나의 파일 저장 MultipartFile을 받아와 UploadFile로 변환
//     */
//    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
//
//        // 비어있으면 널로 반환
//        if (multipartFile.isEmpty()) {
//            return null;
//        }
//
//        // 비어있지 않으면
//        // 사용자가 업로드한 파일명 가져오고 ex) image.png
//        String originalFilename = multipartFile.getOriginalFilename();
//
////        // 확장자를 가져오기 : 나중에 서버에서 구분하기 쉽게하려고 ex) png
////        String ext = extractedExt(originalFilename);
////
////        // 서버에 저장하는 파일명 = 유효Id로 ex) "qwe-qwe-123-qwe-qqwe.png"
////        String uuid = UUID.randomUUID().toString();
////
////        // 최종적으로 저장할 파일명
////        String storeFileName = uuid + "." + ext;
//
//        // 위 작업을 한 메서드를 적용한 것
//        String storeFileName = createStoreFileName(originalFilename);
//
//        // 파일 경로명으로 변환 및 UploadFile 객체로 반환
//        multipartFile.transferTo(new File(getFullPath(storeFileName)));
//        return new UploadFile(originalFilename, storeFileName);
//
//    }


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

//    @Override
//    public Optional<BoardDto> findById(Long id) {
//        Optional<Board> optionalBoardEntity = boardRepository.findById(id);
//        if (optionalBoardEntity.isPresent()) {
//            Board boardEntity = optionalBoardEntity.get();
//            BoardDto boardDto = BoardDto.toBoardDto(boardEntity);
//            return boardDto;
//        } else {
//            return null;
//        }
////        return boardRepository.findById(id);
//    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Override
    public Page<Board> findPageListByBoardType(Pageable pageable, String boardType) {
        return boardRepository.findPageListByBoardType(pageable, boardType);
    }

    @Override
    public Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable, String boardType) {
        log.info("제목 검색");

        return boardRepository.findByTitleContainingAndBoardTypeContaining(titleSearchKeyword, pageable, boardType);
    }

    @Override
    public Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable, String boardType) {
        log.info("작가 검색");

        return boardRepository.findByWriterContainingAndBoardTypeContaining(writerSearchKeyword, pageable, boardType);
    }

    @Override
    public Page<Board> findByTitleContainingAndWriterContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable, String boardType) {
        log.info("작가, 제목 동시 검색");

        return boardRepository.findByTitleContainingAndWriterContainingAndBoardTypeContaining(titleSearchKeyword, writerSearchKeyword, pageable, boardType);
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