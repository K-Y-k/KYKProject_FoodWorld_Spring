package kyk.SpringFoodWorldProject.member.domain.dto;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

/**
 * 회원 수정시 사용될 DTO
 */
@Getter @Setter
public class UpdateForm {

    private Long id;
    private String name;
    private String loginId;
    private String password;


}
