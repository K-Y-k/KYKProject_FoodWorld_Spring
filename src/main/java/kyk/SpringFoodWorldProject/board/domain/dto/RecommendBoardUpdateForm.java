package kyk.SpringFoodWorldProject.board.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class RecommendBoardUpdateForm {
    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 60, message = "최대 50글자입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 500, message = "최대 500글자입니다.")
    private String content;

    private String boardType;

    @NotBlank(message = "카테고리를 선택해주세요")
    private String subType;

    private String area;

    private String menuName;

}
