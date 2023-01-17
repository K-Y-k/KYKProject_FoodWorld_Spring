package kyk.SpringFoodWorldProject.member.domain.entity;

import antlr.build.ANTLR;
import com.fasterxml.classmate.AnnotationOverrides;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * 회원 엔티티
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 7)
    private String name;

    @Column(length = 10)
    private String loginId;

    @Column(length = 10)
    private String password;


    @Builder
    public Member(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    
    // 비즈니스 로직
    // 변경 감지로 프로필 업데이트
    public void changeProfile(String name, String loginId, String password){
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

}



