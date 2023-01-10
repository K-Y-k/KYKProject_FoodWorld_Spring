package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> pageList(Pageable pageable);

    void update(Long commentId);

    void delete(Long commentId);
}
