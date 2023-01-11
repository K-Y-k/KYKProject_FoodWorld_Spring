package kyk.SpringFoodWorldProject.member.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * 회원 엔티티
 */
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    @Column(length = 10)
    private String name;

    @NotEmpty(message = "아이디는 필수입니다.")
    @Column(length = 10)
    private String loginId;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Column(length = 10)
    private String password;

    public Member() {
    }

    public Member(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }
}



