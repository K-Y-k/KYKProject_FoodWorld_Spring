package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

/**
 * QueryDsl을 구현하기 위한 사용자 정의 인터페이스
 */
public interface CommentRepositoryCustom {
    Long findFirstCursorCommentId(Long commentId);
    Slice<Comment> searchBySlice(Long lastCursorBoardId, Boolean first, Pageable pageable, String boardId);
}
