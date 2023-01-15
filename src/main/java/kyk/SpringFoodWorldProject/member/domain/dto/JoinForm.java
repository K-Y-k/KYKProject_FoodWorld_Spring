package kyk.SpringFoodWorldProject.member.domain.dto;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // @Builder에 의해서 생성자가 생성되므로 (매개변수를 가지는 생성자가 하나라도 정의 되어있는 경우) 기본 생성자는 자동으로 추가되지 않는다.
public class JoinForm {
    private Long id;
    @NotEmpty(message = "닉네임을 입력해주세요")
    @Size(min = 6, max = 10)
    private String name;

    @Size(min = 6, max = 10, message = "최소 6글자 최대 10글자입니다.")
    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;

    @Size(min = 6, max = 10)
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;


    // 생성자 레벨에 @Builder을 작성해주게되면
    // 해당 필드 값들에 대해서 위와같은 Builder class를 자동으로 생성해준다.
    @Builder
    public JoinForm(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .loginId(loginId)
                .password(password)
                .build();
    }

}
