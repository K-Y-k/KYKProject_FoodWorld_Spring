package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.repository.BoardFileRepository;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    @Value("${file.imageFileLocation}")
    private String imageFileLocation;

    @Value("${file.attachFileLocation}")
    private String attachFileLocation;


    @Override
    public Long upload(Long memberId, BoardUploadForm boardDto) throws IOException {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("글 등록 실패: 로그인 상태가 아닙니다." + memberId));

        List<MultipartFile> attachFiles = boardDto.getAttachFiles();
        List<MultipartFile> imageFiles = boardDto.getImageFiles();

        // 첨부파일이 있을 경우
        if (!attachFiles.get(0).getOriginalFilename().isBlank()) {
            // 여러 개의 파일일 수 있으므로 부모 객체인 Board부터 가져와야함
            // + attached 속성 1로 설정한 toSaveFileEntity로 글 저장
            Board boardEntity = boardDto.toSaveFileEntity(findMember, boardDto);
            Long savedId = boardRepository.save(boardEntity).getId();
            Board board = boardRepository.findById(savedId).get();

            attachUpload(boardDto, board);

            // + 이미지파일이 있을 경우
            if (imageFiles.get(0).getOriginalFilename() != null && !imageFiles.get(0).getOriginalFilename().isBlank()) {
                imageUpload(boardDto, board);
            }

            return savedId;
        } else if (attachFiles.get(0).getOriginalFilename().isBlank()) {
            // 첨부파일이 없고 이미지파일은 있을 경우
            if (!imageFiles.get(0).getOriginalFilename().isBlank()) {
                Board boardEntity = boardDto.toSaveFileEntity(findMember, boardDto);
                Long savedId = boardRepository.save(boardEntity).getId();
                Board board = boardRepository.findById(savedId).get();

                imageUpload(boardDto, board);
                return savedId;
            } else { // 첨부파일, 이미지파일 모두 없을 경우
                Board uploadBoard = uploadBoard(boardDto, findMember);
                return uploadBoard.getId();
            }
        }
        return null;
    }


    private Board uploadBoard(BoardUploadForm boardDto, Member findMember) {
        Board board = boardDto.toSaveEntity(findMember, boardDto);
        Board uploadBoard = boardRepository.save(board);
        log.info("uploadBoard={}", board);
        return uploadBoard;
    }

    private void imageUpload(BoardUploadForm boardDto, Board board) throws IOException {
        // 루프를 돌려 파일을 모두 찾고 반환
        for (MultipartFile imageFiles: boardDto.getImageFiles()) {
            String originalFilename = imageFiles.getOriginalFilename();

            // 파일에 이름을 붙일 랜덤으로 식별자 지정
            UUID uuid = UUID.randomUUID();
            String storedFileName = uuid + "_" + originalFilename;
            String savePath = imageFileLocation;

            // 실제 파일 저장 경로와 파일 이름 지정한 File 객체 생성 및 저장
            imageFiles.transferTo(new File(savePath, storedFileName));

            String attachedType = "none";

            // DB에 파일 관련 필드 값 저장
            BoardFile boardFileEntity = BoardFile.toBoardFileEntity(board, originalFilename, storedFileName, attachedType);
            boardFileRepository.save(boardFileEntity);
        }
    }

    private void attachUpload(BoardUploadForm boardDto, Board board) throws IOException {
        // 루프를 돌려 파일을 모두 찾고 반환
        for (MultipartFile attachFiles: boardDto.getAttachFiles()) {
            String originalFilename = attachFiles.getOriginalFilename();

            // 파일에 이름을 붙일 랜덤으로 식별자 지정
            UUID uuid = UUID.randomUUID();
            String storedFileName = uuid + "_" + originalFilename;
            String savePath = attachFileLocation;

            // 실제 파일 저장 경로와 파일 이름 지정한 File 객체 생성 및 저장
            attachFiles.transferTo(new File(savePath, storedFileName));

            String attachedType = "attached";

            // DB에 파일 관련 필드 값 저장
            BoardFile boardFileEntity = BoardFile.toBoardFileEntity(board, originalFilename, storedFileName, attachedType);
            boardFileRepository.save(boardFileEntity);
        }
    }

//    // 확장자 구분 메서드
//    private String createStoreFileName(String originalFilename) {
//        String ext = extractedExt(originalFilename);
//        String uuid = UUID.randomUUID().toString();
//        return uuid + "." + ext;
//
//    }
//    private String extractedExt(String originalFilename) {
//        // 마지막 .뒤의 위치를 가져온다.
//        int pos = originalFilename.lastIndexOf(".");
//        // 위를 활용하여 확장자를 가져온다.
//        return originalFilename.substring(pos + 1);
//    }



    @Override
    public Long updateBoard(Long boardId, BoardUpdateForm updateParam) {
        Board findBoard = findById(boardId).orElseThrow();

        log.info("서브 타입 : ", findBoard.getSubType());

        findBoard.updateBoard(updateParam.getTitle(), updateParam.getContent(), updateParam.getSubType());


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

    public Optional<BoardFile> findBoardFileById(Long boardFileId){
        return boardFileRepository.findById(boardFileId);
    }


    @Override
    public Long updateCommentCount(Long boardId) {
        Board findBoard = findById(boardId).orElseThrow();

        findBoard.updateCommentCount(commentRepository.findCommentCount(findBoard.getId()));

        log.info("댓글 개수 갱신완료");
        return findBoard.getId();
    }


}