package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Page<Comment> findPageListByBoardId(Pageable pageable, Long boardId);

    Optional<Comment> findById(Long id);
    List<Comment> findAll();

    void delete(Long commentId);

    int findCommentCount(Long boardId);

    Long findFirstCursorBoardId(Long boardId);
    Slice<Comment> searchBySlice(Long lastCursorBoardId, Boolean first, Pageable pageable, String boardId);

}
