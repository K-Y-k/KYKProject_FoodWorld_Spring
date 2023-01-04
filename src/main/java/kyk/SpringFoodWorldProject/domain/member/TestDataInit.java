package kyk.SpringFoodWorldProject.domain.member;

import kyk.SpringFoodWorldProject.domain.member.entity.Member;
import kyk.SpringFoodWorldProject.domain.member.repository.MemoryMemberRepository;
import kyk.SpringFoodWorldProject.domain.member.repository.SpringDataJpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class TestDataInit {
    private final SpringDataJpaMemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 회원 데이터 추가
        memberRepository.save(new Member("테스터1", "test1", "test!"));

    }

}
