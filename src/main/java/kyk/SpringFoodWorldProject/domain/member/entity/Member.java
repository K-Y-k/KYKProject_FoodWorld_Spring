package kyk.SpringFoodWorldProject.domain.member.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data// @Data는 다양한 기능이 포함되어 핵심 도메인 모델에서는 사용이 위험하다. DTO(데이터 이동에만 사용하는) 같은 곳에서만 쓰기 (Getter Setter, RequiredArsConstrutor, @ToString, @EqualAndHashCode 등 다양한 것이 포함되었기에)
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



