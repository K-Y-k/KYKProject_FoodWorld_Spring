package kyk.SpringFoodWorldProject.follow.repository;

import kyk.SpringFoodWorldProject.follow.domain.dto.FollowDto;
import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface FollowRepositoryCustom {
    Long findFirstCursorFollowerId(Member member);
    Slice<FollowDto> searchBySlice(Member member, Long lastCursorFollowerId, Boolean first, Pageable pageable);
    List<Member> recommendMember(Long currentMemberId);
}
