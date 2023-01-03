package kyk.SpringFoodWorldProject.domain.member.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data // @Data는 다양한 기능이 포함되어 핵심 도메인 모델에서는 사용이 위험하다. DTO(데이터 이동에만 사용하는) 같은 곳에서만 쓰기 (Getter Setter, RequiredArsConstrutor, @ToString, @EqualAndHashCode 등 다양한 것이 포함되었기에)
public class Member {

    private Long id;

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

}



//@Entity
//@Getter @Setter
//public class Member {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name="NAME")
//    private String userName;
//
//    private int age;
//
//    public Member(Long id, String userName, int age) {
//        this.id = id;
//        this.userName = userName;
//        this.age = age;
//    }
//
//}
