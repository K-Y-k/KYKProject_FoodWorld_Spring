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

        int i=0;

        while (i < 2) {
            memberRepository.save(new Member("테스터" + i, "test" + i, "test!" + i));
            i++;
        }
        memberRepository.save(new Member("aaa", "aa", "aa"));

        while (i < 20) {
            boardRepository.save(new Board("제목" + (i+2), "내용" + i, "작가" + (i+1)));
            i++;
        }
        boardRepository.save(new Board("제목ddddddcdddddddddddddddddㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇdddddddddddddd", "내용" , "작가10"));

    }

}
