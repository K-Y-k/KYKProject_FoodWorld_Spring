package kyk.SpringFoodWorldProject.comment.domain.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CommentUpdateDto {
    @NotEmpty(message = "내용을 입력해주세요")
    @Size(max = 250, message = "최대 250글자입니다.")
    private String content;

}
