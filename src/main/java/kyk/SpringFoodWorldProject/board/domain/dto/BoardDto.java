package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 글 저장 전송객체
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long id;

    @NotEmpty(message = "제목을 입력해주세요")
    @Size(max = 60, message = "최대 50글자입니다.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    @Size(max = 500, message = "최대 500글자입니다.")
    private String content;

}
