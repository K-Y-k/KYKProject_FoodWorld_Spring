package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.dto.MucstarUploadForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BoardService {

    /**
     * 일반 게시판 글 저장
     */
    Long upload(Long memberId, BoardUploadForm boardDto) throws IOException;

    /**
     * 먹스타그램 글 저장
     */
    Long muckstarUpload(Long memberId, MucstarUploadForm boardDto) throws IOException;

    /**
     * 글 수정
     */
    Long updateBoard(Long boardId, BoardUpdateForm boardUpdateDto);

    /**
     * 글 id로 조회
     */
    Optional<Board> findById(Long id);

    /**
     * 글 모두 조회
     */
    List<Board> findAll();

    /**
     * 글 페이징 처리
     */
    Page<Board> findPageListByBoardType(Pageable pageable, String boardType);

    /**
     * 글 페이징 + 검색
     */
    Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable, String boardType);

    Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable, String boardType);

    Page<Board> findByTitleContainingAndWriterContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable, String boardType);

    /**
     * 마지막 id 조회
     */
    Long findFirstCursorBoardId(String boardType);
    Long findFirstCursorBoardIdInMember(Long memberId, String boardType);

    /**
     * 무한 스크롤 페이징 처리
     */
    Slice<Board> searchBySlice(String memberId, Long lastBoardId, Boolean first, BoardSearchCond boardSearchCond, Pageable pageable, String boardType);
    
    
    /**
     * 글 삭제
     */
    void delete(Long boardId);


    /**
     * 조회수 카운트
     */
    int updateCount(Long boardId);


    /**
     * 좋아요 개수 최신화
     */
    void updateLikeCount(Long boardId, int likeCount);


    /**
     * 댓글 개수 최신화
     */
    Long updateCommentCount(Long boardId);
}


