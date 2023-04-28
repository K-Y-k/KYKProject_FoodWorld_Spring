package kyk.SpringFoodWorldProject.member.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원 엔티티
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 7)
    private String name;

    @Column(length = 10)
    private String loginId;

    @Column(length = 10)
    private String password;

    private String introduce;

    @JsonIgnoreProperties({"member"})
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private ProfileFile profileFile;

    private int followCount;

    private int followingCount;

    private String role;


    @Builder
    public Member(String name, String loginId, String password, String role) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

    
    // 비즈니스 로직
    // 변경 감지로 프로필 업데이트
    public void changeProfile(String name, String loginId, String password, String introduce){
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.introduce = introduce;
    }

}



