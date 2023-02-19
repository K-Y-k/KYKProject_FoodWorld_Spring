package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JPABoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    /**
     * 키워드 검색에 따른 검색
     */
    Page<Board> findPageListByBoardType(Pageable pageable, String boardType);
    Page<Board> findByTitleContainingAndBoardTypeContaining(String titleSearchKeyword, Pageable pageable, String boardType);
    Page<Board> findByWriterContainingAndBoardTypeContaining(String writerSearchKeyword, Pageable pageable, String boardType);
    Page<Board> findByTitleContainingAndWriterContainingAndBoardTypeContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable, String boardType);


    // 트랜잭션 스크립트 패턴
    /**
     * 조회수 카운트
     */
    @Modifying // @Query 어노테이션에서 작성된 select를 제외한 insert, update, delete 쿼리 사용시 필요한 어노테이션
    @Query("update Board b set b.count = b.count + 1 where b.id = :boardId")
    int updateCount(Long boardId);

}
