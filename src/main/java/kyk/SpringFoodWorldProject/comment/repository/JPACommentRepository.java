package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPACommentRepository extends JpaRepository<Comments, Long> {
}
