package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
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
    public List<Comment> pageList(Pageable pageable) {
        return null;
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


}
