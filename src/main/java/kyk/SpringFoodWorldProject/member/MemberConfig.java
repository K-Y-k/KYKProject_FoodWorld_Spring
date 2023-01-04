package kyk.SpringFoodWorldProject.member;

import kyk.SpringFoodWorldProject.member.repository.JPAMemberRepository;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
import kyk.SpringFoodWorldProject.member.service.MemberService;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 스프링 데이터 JPA로 구현한 회원 리포지토리 설정
 */
@Configuration
@RequiredArgsConstructor
public class MemberConfig {
    private final JPAMemberRepository repository;

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new SpringDataJpaMemberRepository(repository);
    }

}
