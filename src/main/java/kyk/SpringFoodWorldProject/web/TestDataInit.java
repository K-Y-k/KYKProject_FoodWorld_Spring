package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class TestDataInit {
    private final SpringDataJpaMemberRepository memberRepository;
    private final SpringDataJpaBoardRepository boardRepository;

    private final CommentServiceImpl commentService;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 회원 데이터 추가
        memberRepository.save(new Member("테스터1", "test", "test!"));
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));


        // 게시글 데이터 추가
        int boardCount = 1;
        while (boardCount < 20) {
            boardRepository.save(new Board("제목" + (boardCount+2), "내용" + boardCount, "작가" + (boardCount+1), savedMember1));
            boardCount++;
        }
        Board savedBoard = boardRepository.save(new Board("제목ddddddcdddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇddd", "내용", "작가10", savedMember2));


        // 댓글 데이터 추가
        commentService.saveComment(savedMember1.getId(), savedBoard.getId(), new CommentDto(1L,"안녕하세요"));
        commentService.saveComment(savedMember2.getId(), savedBoard.getId(), new CommentDto(2L,"안녕하세요2"));


    }

}
