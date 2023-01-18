package kyk.SpringFoodWorldProject.like.service;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.like.domain.dto.LikeDto;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import kyk.SpringFoodWorldProject.like.repository.LikeRepository;
import kyk.SpringFoodWorldProject.like.repository.LikeRepositoryImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;


    @Override
    public int saveLike(Long memberId, Long boardId) {
        Optional<Like> findLike = likeRepository.findByMember_IdAndBoard_Id(memberId, boardId);

        log.info("findLike.isEmpty() = {}", findLike.isEmpty());

        if (findLike.isEmpty()){
            Member memberEntity = memberRepository.findById(memberId).get();
            Board boardEntity = boardRepository.findById(boardId).get();

            Like likeEntity = LikeDto.toEntity(memberEntity, boardEntity);
            likeRepository.save(likeEntity);

            return 1;
        } else {
            likeRepository.delete(findLike.get().getId());
            return 0;
        }
    }

    @Override
    public int countByBoard_Id(Long boardId) {
        return likeRepository.countByBoard_Id(boardId);
    }

}