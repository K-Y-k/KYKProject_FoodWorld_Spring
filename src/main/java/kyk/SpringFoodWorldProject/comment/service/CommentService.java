package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentService {

    void save(Comment comment);
    List<Comment> pageList(Pageable pageable);

    void update(Comment comment);

    void delete(Long commentId);
}
