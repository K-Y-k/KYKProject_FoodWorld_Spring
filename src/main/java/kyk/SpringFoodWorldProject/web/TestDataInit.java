package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
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

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 회원 데이터 추가
        int memberCount = 1;

        while (memberCount < 3) {
            memberRepository.save(new Member("테스터" + memberCount, "test" + memberCount, "test!" + memberCount));
            memberCount++;
        }
        memberRepository.save(new Member("aaa", "aa", "aa"));


        // 게시글 데이터 추가
        int boardCount = 1;
        while (boardCount < 20) {
            boardRepository.save(new Board("제목" + (boardCount+2), "내용" + boardCount, "작가" + (boardCount+1)));
            boardCount++;
        }
        boardRepository.save(new Board("제목ddddddcdddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇddd", "내용" , "작가10"));

    }

}
