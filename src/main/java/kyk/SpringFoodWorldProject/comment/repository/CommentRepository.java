package kyk.SpringFoodWorldProject.comment.repository;

import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUpdateDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comments;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comments save(Comments comment);
    List<Comments> pageList(Pageable pageable);

    void update(Long commentId, CommentUpdateDto commentUpdateDto);

    Optional<Comments> findById(Long id);
    List<Comments> findAll();

    void delete(Long commentId);


}
