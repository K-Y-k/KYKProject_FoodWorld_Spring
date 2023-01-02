package kyk.SpringFoodWorldProject.domain.member.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String userName;

    private int age;

    public Member(Long id, String userName, int age) {
        this.id = id;
        this.userName = userName;
        this.age = age;
    }



}
