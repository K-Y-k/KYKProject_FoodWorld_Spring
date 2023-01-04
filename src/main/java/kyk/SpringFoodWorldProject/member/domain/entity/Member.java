package kyk.SpringFoodWorldProject.member.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * 회원 엔티티
 */
@Getter @Setter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(length = 10)
    private String name;

    @NotEmpty
    @Column(length = 10)
    private String loginId;
    @NotEmpty
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



