package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> pageList(Pageable pageable);

    Optional<Comment> findById(Long id);
    List<Comment> findAll();

    void delete(Long commentId);


}
