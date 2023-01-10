package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    /**
     * 글 저장
     */
    Board save(Board board);

    /**
     * 글 수정
     */
    void update(Long boardId, BoardUpdateDto boardUpdateDto);

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
    Page<Board> pageList(Pageable pageable);

    /**
     * 글 검색
     */
    Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable);
    Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable);
    Page<Board> findByTitleContainingAndWriterContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable);

    /**
     * 글 삭제
     */
    void delete(Long boardId);

    /**
     * 조회수 카운트
     */
    int updateCount(Long boardId);


}
