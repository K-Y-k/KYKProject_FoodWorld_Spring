package kyk.SpringFoodWorldProject.domain.member;

import kyk.SpringFoodWorldProject.domain.member.entity.Member;
import kyk.SpringFoodWorldProject.domain.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 회원 데이터 추가
        Member member = new Member();
        member.setLoginId("test1");
        member.setPassword("test!"); // !는 요즘 크롬에서 비밀번호 안전 경고로
        member.setName("테스터1");
        memberRepository.save(member);

    }

}
