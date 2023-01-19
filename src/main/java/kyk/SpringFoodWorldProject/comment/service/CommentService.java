package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
    List<Comment> pageList(Pageable pageable);

    void updateComment(Long commentId, CommentUpdateDto updateParam);
    void delete(Long commentId);
}
