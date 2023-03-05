package kyk.SpringFoodWorldProject.follow.repository;

import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository{

    private final JpaFollowRepository followRepository;

    @Override
    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }

    @Override
    public Optional<Follow> findByFromMember_IdAndToMember_Id(Long fromMemberId, Long toMemberId) {
        return followRepository.findByFromMember_IdAndToMember_Id(fromMemberId, toMemberId);
    }

    @Override
    public int countByToMember_Id(Long toMemberId) {
        return followRepository.countByToMember_Id(toMemberId);
    }

    @Override
    public int countByFromMember_Id(Long fromMemberId) {
        return followRepository.countByFromMember_Id(fromMemberId);
    }

    @Override
    public void deleteByFromMember_IdAndToMember_Id(Long fromMemberId, Long toMemberId) {
        followRepository.deleteByFromMember_IdAndToMember_Id(fromMemberId, toMemberId);
    }
}
