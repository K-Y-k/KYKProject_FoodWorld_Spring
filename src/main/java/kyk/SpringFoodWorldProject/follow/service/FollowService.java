package kyk.SpringFoodWorldProject.follow.service;

import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface FollowService {

    /**
     * 팔로우
     */
    int followAndUnFollow(Long fromMemberId, Long toMemberId);

    /**
     * 회원 ID와 상대 회원 둘다 있는지 조회
     */
    Optional<Follow> findByFromMember_IdAndToMember_Id(Long fromMemberId, Long toMemberId);


    /**
     * 팔로우 수 조회
     */
    int countByToMember_Id(Long toMemberId);

    /**
     * 팔로잉 수 조회
     */
    int countByFromMember_Id(Long fromMemberId);

    Long findFirstCursorFollowerId(Member member);
    Slice<Follow> searchBySlice(Member member, Long lastCursorFollowerId, Boolean first, Pageable pageable);
    List<Member> recommendMember(Long currentMemberId);

}
