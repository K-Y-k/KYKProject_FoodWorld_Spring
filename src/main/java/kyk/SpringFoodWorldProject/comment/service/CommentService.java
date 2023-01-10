package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentService {

    Comments save(Comments comment);
    List<Comments> pageList(Pageable pageable);

    void update(Long commentId, CommentUpdateDto updateParam);

    void delete(Long commentId);
}
