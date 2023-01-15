package kyk.SpringFoodWorldProject.member.domain.dto;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 회원 수정시 사용될 DTO
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
public class UpdateForm {

    private Long id;

    @NotEmpty(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 7, message = "최소 2글자, 최대 7글자입니다.")
    private String name;

    @Size(min = 3, max = 10, message = "최소 3글자, 최대 10글자입니다.")
    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;

    @Size(min = 3, max = 10, message = "최소 3글자, 최대 10글자입니다.")
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

}
