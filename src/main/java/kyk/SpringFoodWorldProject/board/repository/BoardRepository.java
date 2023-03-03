package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

/**
 * 구현체가 바뀌어도 같은 기능들을 선언하게 인터페이스를 만듬
 */
public interface BoardRepository {
    /**
     * 글 저장
     */
    Board save(Board board);

//    Board upload(String name, BoardDto board);


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
    Page<Board> findByTitleContainingAndBoardTypeContaining(String titleSearchKeyword, Pageable pageable, String boardType);
    Page<Board> findByWriterContainingAndBoardTypeContaining(String writerSearchKeyword, Pageable pageable, String boardType);
    Page<Board> findByTitleContainingAndWriterContainingAndBoardTypeContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable, String boardType);


    /**
     * 최근 boardId 조회
     */
    Long findFirstCursorBoardId(String boardType);
    Long findFirstCursorBoardIdInMember(Long memberId, String boardType);

    /**
     * 무한 스크롤 페이징
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
     * 한 회원이 작성한 게시글 수 카운트
     */
    int boardsTotalCount(Long memberId);


}
