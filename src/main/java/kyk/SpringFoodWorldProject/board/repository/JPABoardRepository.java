package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPABoardRepository extends JpaRepository<Board, Long> {
    /**
     * 키워드 검색에 따른 검색
     */
    Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable);
    Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable);

}
