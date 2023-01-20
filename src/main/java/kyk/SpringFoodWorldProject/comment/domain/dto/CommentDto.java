package kyk.SpringFoodWorldProject.comment.domain.dto;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private String writer;
    private Long boardId;


//    public CommentDto(Comment comment) {
//        this.id = comment.getId();
//        this.content = comment.getContent();
//        this.writer = comment.getWriter();
//        this.createdDate = comment.getCreatedDate();
//        this.boardId = comment.getBoard().getId();
//    }



}
