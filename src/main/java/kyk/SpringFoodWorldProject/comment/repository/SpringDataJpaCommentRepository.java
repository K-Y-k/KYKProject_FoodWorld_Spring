package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaCommentRepository implements CommentRepository{

    private final JPACommentRepository repository;

    @Override
    public Comment save(Comment comment) {
       return repository.save(comment);
    }

    @Override
    public List<Comment> pageList(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long commentId) {
        repository.save(commentId);
    }

    @Override
    public void delete(Long commentId) {

    }

    private final

}
