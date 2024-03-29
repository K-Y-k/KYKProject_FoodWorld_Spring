package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.admin.dto.AdminBoardDTO;
import kyk.SpringFoodWorldProject.board.domain.dto.*;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
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
    Long recommendUpdateBoard(Long boardId, RecommendBoardUpdateForm updateParam);
    Long muckstarUpdateBoard(Long boardId, MuckstarUpdateForm updateParam);

    Long updateBoard(Long boardId, FreeBoardUpdateForm freeBoardUpdateForm, RecommendBoardUpdateForm recommendBoardUpdateForm, MuckstarUpdateForm muckstarUpdateForm) throws IOException;

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
    Slice<Board> searchBySlice(String memberId, Long lastBoardId, Boolean first, Pageable pageable, String boardType);
    Slice<Board> searchBySliceByWriter(String memberId, Long lastCursorBoardId, Boolean first, String writerSearchKeyword, Pageable pageable, String boardType);
    
    /**
     * 글 삭제
     */
    void delete(Long boardId, String boardType);


    /**
     * 글 조회수 카운트
     */
    int updateCount(Long boardId);

    /**
     * 한 회원이 작성한 게시글 수 카운트
     */
    int boardsTotalCount(Long memberId);

    /**
     * 좋아요 개수 최신화
     */
    void updateLikeCount(Long boardId, int likeCount);


    /**
     * 댓글 개수 최신화
     */
    Long updateCommentCount(Long boardId);


    List<Board> popularBoardList(String boardType);

    Page<Board> categoryBoardList(String boardType, BoardSearchCond boardSearchDto, Pageable pageable);
}


