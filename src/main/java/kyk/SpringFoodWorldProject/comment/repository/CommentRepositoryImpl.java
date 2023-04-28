package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository{

    private final JPACommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
       return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> findPageListByBoardId(Pageable pageable, Long boardId) {
        return commentRepository.findPageListByBoardId(pageable, boardId);
    }


    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public int findCommentCount(Long boardId) {
        return commentRepository.findCommentCount(boardId);
    }

    @Override
    public Long findFirstCursorBoardId(Long boardId) {
        return commentRepository.findFirstCursorBoardId(boardId);
    }

    @Override
    public Slice<Comment> searchBySlice(Long lastCursorBoardId, Boolean first, Pageable pageable, String boardId) {
        return commentRepository.searchBySlice(lastCursorBoardId, first, pageable, boardId);
    }


}
