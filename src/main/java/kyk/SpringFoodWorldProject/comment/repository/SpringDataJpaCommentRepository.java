package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaCommentRepository implements CommentRepository{

    private final JPACommentRepository commentRepository;

    @Override
    public Comments save(Comments comment) {
       return commentRepository.save(comment);
    }

    @Override
    public List<Comments> pageList(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long commentId, CommentUpdateDto updateParam) {
        Comments findComment = commentRepository.findById(commentId).orElseThrow((() ->
                new IllegalStateException("해당 댓글이 존재하지 않습니다.")));

        findComment.setContent(updateParam.getContent());
    }

    @Override
    public Optional<Comments> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comments> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }


}
