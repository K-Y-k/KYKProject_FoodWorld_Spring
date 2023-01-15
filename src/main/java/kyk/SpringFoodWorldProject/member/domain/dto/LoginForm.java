package kyk.SpringFoodWorldProject.member.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginForm {
    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;
}
