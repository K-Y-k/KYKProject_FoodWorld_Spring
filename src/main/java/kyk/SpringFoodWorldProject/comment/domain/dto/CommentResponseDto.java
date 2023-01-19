package kyk.SpringFoodWorldProject.comment.domain.dto;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private String writer;
    private Long boardId;

    // Entity -> Dto
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getWriter();
        this.boardId = comment.getBoard().getId();
    }



}
