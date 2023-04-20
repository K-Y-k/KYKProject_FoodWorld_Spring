package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * 글 수정시 전송될 전송객체
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class BoardUpdateForm {
    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 60, message = "최대 50글자입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 500, message = "최대 500글자입니다.")
    private String content;

    private String boardType;

    private String subType;

    private String area;

    private String menuName;

}
