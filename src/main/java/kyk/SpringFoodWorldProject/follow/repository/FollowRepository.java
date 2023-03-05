package kyk.SpringFoodWorldProject.follow.repository;

import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;

import java.util.Optional;

public interface FollowRepository {

    /**
     * 팔로우
     */
    Follow save(Follow follow);

    /**
     * 회원 ID와 게시글 ID 둘다 있는지 조회
     */
    Optional<Follow> findByFromMember_IdAndToMember_Id(Long fromMemberId, Long toMemberId);


    /**
     * 팔로워 수 조회
     */
    int countByToMember_Id(Long toMemberId);

    /**
     * 팔로잉 수 조회
     */
    int countByFromMember_Id(Long fromMemberId);


    /**
     * 팔로우 취소
     */
    void deleteByFromMember_IdAndToMember_Id(Long fromMemberId, Long toMemberId);
}
