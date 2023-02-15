package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
     * 글 검색
     */
    Page<Board> findByTitleContainingAndBoardTypeContaining(String titleSearchKeyword, Pageable pageable, String boardType);
    Page<Board> findByWriterContainingAndBoardTypeContaining(String writerSearchKeyword, Pageable pageable, String boardType);
    Page<Board> findByTitleContainingAndWriterContainingAndBoardTypeContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable, String boardType);

    /**
     * 글 삭제
     */
    void delete(Long boardId);

    /**
     * 조회수 카운트
     */
    int updateCount(Long boardId);


}
