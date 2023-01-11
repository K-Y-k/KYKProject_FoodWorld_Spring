package kyk.SpringFoodWorldProject.member.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

/**
 * 회원 수정시 사용될 DTO
 */
@Getter @Setter
public class MemberDto {

    private Long id;
    private String name;
    private String loginId;
    private String password;

    public Member toEntity() {
        Member member = Member.builder()
                .id(id)
                .name(name)
                .loginId(loginId)
                .password(password)
                .build();
        return member;
    }


}
