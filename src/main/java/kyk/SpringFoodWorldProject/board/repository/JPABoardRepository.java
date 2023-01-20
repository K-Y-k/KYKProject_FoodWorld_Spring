package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JPABoardRepository extends JpaRepository<Board, Long> {
    /**
     * 키워드 검색에 따른 검색
     */
    Page<Board> findByTitleContaining(String titleSearchKeyword, Pageable pageable);
    Page<Board> findByWriterContaining(String writerSearchKeyword, Pageable pageable);
    Page<Board> findByTitleContainingAndWriterContaining(String titleSearchKeyword, String writerSearchKeyword, Pageable pageable);


    // 트랜잭션 스크립트 패턴
    /**
     * 조회수 카운트
     */
    @Modifying // @Query 어노테이션에서 작성된 select를 제외한 insert, update, delete 쿼리 사용시 필요한 어노테이션
    @Query("update Board b set b.count = b.count + 1 where b.id = :boardId")
    int updateCount(Long boardId);


//    @Modifying
//    @Transactional
//    @Query(value = "insert into Board b (b.id, b.title, b.content, b.member.name) VALUES(:#{boardDto.id}, :#{boardDto.title}, :#{boardDto.content}, :#{boardDto.member.name}, 0, 0, :#{boardDto.member.id}, null)", nativeQuery = true)
//    int upload(@Param("boardDto") BoardDto boardDto);


}
