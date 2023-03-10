package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.repository.BoardFileRepository;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUploadDto;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Profile("local")
@Controller
@RequiredArgsConstructor
public class TestDataInit {
    private final SpringDataJpaMemberRepository memberRepository;
    private final SpringDataJpaBoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    private final CommentServiceImpl commentService;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 회원 데이터 추가 3
        memberRepository.save(new Member("테스터1", "test", "test!"));
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));


        // 게시글 데이터 추가 20
        int boardCount = 1;
        while (boardCount < 20) {
            boardRepository.save(new Board("제목" + (boardCount+2), "내용" + boardCount, "작가" + (boardCount+1), savedMember1, "자유게시판", "사는얘기"));
            boardCount++;
        }
        Board savedBoard = boardRepository.save(new Board("제목ddddddcdddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇddd", "내용", "작가10", savedMember2, "추천게시판","식당"));


        // 먹스타그램 데이터 추가 10
        int muckstarCount = 1;
        while (muckstarCount < 10) {
            Board savedMuckstar = boardRepository.save(new Board("제목" + muckstarCount, "내용" + muckstarCount, "작가" + muckstarCount, savedMember1, "먹스타그램", "말머리 선택"));
            muckstarCount++;
            commentService.saveComment(savedMember1.getId(), savedMuckstar.getId(), new CommentUploadDto(1L,"안녕하세요"));
            boardFileRepository.save(new BoardFile("0d503084-fb24-49e2-b97c-ce79921529e6_zoom 가상배경.jpg", savedMuckstar));
        }

        // 댓글 데이터 추가 2
        commentService.saveComment(savedMember1.getId(), savedBoard.getId(), new CommentUploadDto(1L,"안녕하세요"));
        commentService.saveComment(savedMember2.getId(), savedBoard.getId(), new CommentUploadDto(2L,"안녕하세요2"));
    }

}
