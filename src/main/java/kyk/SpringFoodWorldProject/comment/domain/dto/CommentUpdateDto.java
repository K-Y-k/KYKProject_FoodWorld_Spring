package kyk.SpringFoodWorldProject.comment.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentUpdateDto {

    private String content;

    public CommentUpdateDto() {
    }

    public CommentUpdateDto(String content) {
        this.content = content;
    }
}
