package kyk.SpringFoodWorldProject.member.domain.dto;

import lombok.Data;

/**
 * 회원 수정시 사용될 DTO
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
