package kyk.SpringFoodWorldProject.follow.domain.dto;

import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.member.domain.dto.MemberDto;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class FollowDto {

    private Long id;

    private MemberDto fromMember;

    private MemberDto toMember;


    // 엔티티에 @setter를 사용하지 않기 위해 dto에서 엔티티로 변환해주는 메서드 적용
    public static Follow toEntity(Member fromMember, Member toMember) {
        return Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
    }

}
