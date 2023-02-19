package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUploadDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;

import java.awt.print.Pageable;
import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
    Long saveComment(Long memberId, Long boardId, CommentUploadDto dto);

    List<Comment> pageList(Pageable pageable);

    void updateComment(Long commentId, CommentUpdateDto updateParam);

    int findCommentCount(Long boardId);

    void delete(Long commentId);

}
