package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUploadDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.comment.repository.CommentRepositoryCustomImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
    Long saveComment(Long memberId, Long boardId, CommentUploadDto dto);

    Page<Comment> findPageListByBoardId(Pageable pageable, Long boardId);

    void updateComment(Long commentId, CommentUpdateDto updateParam);

    int findCommentCount(Long boardId);

    void delete(Long commentId);

    Long findFirstCursorCommentId(Long commentId);
    Slice<Comment> searchBySlice(Long lastCursorBoardId, Boolean first, Pageable pageable, String boardId);

}
