package kyk.SpringFoodWorldProject.board;

import kyk.SpringFoodWorldProject.board.repository.BoardRepository;
import kyk.SpringFoodWorldProject.board.repository.JPABoardRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.board.service.BoardService;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.like.repository.JPALikeRepository;
import kyk.SpringFoodWorldProject.like.repository.LikeRepository;
import kyk.SpringFoodWorldProject.like.repository.LikeRepositoryImpl;
import kyk.SpringFoodWorldProject.like.service.LikeService;
import kyk.SpringFoodWorldProject.like.service.LikeServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.JPAMemberRepository;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BoardConfig {

    private final JPABoardRepository boardRepository;
    private final JPAMemberRepository memberRepository;

    private final JPALikeRepository likeRepository;

    @Bean
    public LikeService likeService() {
        return new LikeServiceImpl(likeRepository(), memberRepository(), boardRepository());
    }

    @Bean
    public BoardService boardService() {
        return new BoardServiceImpl(boardRepository(), memberRepository());
    }

    @Bean
    public BoardRepository boardRepository() {
        return new SpringDataJpaBoardRepository(boardRepository);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new SpringDataJpaMemberRepository(memberRepository);
    }

    @Bean
    public LikeRepository likeRepository() {
        return new LikeRepositoryImpl(likeRepository);
    }

}
