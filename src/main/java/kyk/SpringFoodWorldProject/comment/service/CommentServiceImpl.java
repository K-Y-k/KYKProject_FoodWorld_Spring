package kyk.SpringFoodWorldProject.comment.service;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository repository;

    @Override
    public void save(Comment comment) {

    }

    @Override
    public List<Comment> pageList(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Comment comment) {

    }

    @Override
    public void delete(Long commentId) {

    }
}
