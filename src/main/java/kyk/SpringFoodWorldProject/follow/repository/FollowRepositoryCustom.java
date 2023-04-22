package kyk.SpringFoodWorldProject.follow.repository;

import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FollowRepositoryCustom {
    Long findFirstCursorFollowerId(Member member);
    Slice<Follow> searchBySlice(Member member, Long lastCursorFollowerId, Boolean first, Pageable pageable);
}
