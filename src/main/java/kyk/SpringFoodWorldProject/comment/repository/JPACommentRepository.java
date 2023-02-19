package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JPACommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 댓글 개수 조회
     */
    @Query("select count(*) from Comment c where c.board.id = :boardId")
    int findCommentCount(Long boardId);
}
