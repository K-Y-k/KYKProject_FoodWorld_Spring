package kyk.SpringFoodWorldProject.like.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.like.domain.dto.LikeDto;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository{

    private final JPALikeRepository likeRepository;
    
    @Override
    public Like save(Like like) {
        return likeRepository.save(like);
    }

    @Override
    public Optional<Like> findByMember_IdAndBoard_Id(Long memberId, Long boardId) {
        return likeRepository.findByMember_IdAndBoard_Id(memberId, boardId);
    }

    @Override
    public void delete(Long likeId) {
        likeRepository.deleteById(likeId);
    }

    @Override
    public int countByBoard_Id(Long boardId) {
        return likeRepository.countByBoard_Id(boardId);
    }

}
