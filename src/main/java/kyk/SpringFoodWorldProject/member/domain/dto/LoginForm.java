package kyk.SpringFoodWorldProject.member.domain.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginForm {
    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;
}
