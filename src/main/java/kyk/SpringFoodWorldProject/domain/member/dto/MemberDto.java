package kyk.SpringFoodWorldProject.domain.member.dto;

import lombok.Data;

/**
 * 회원 수정시 사용될 Dto
 */
@Data
public class MemberDto {

    private String name;
    private String loginId;
    private String password;

    public MemberDto() {
    }

    public MemberDto(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }
}
