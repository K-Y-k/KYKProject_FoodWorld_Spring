package kyk.SpringFoodWorldProject.domain.member.config;

import kyk.SpringFoodWorldProject.domain.member.repository.JPAMemberRepository;
import kyk.SpringFoodWorldProject.domain.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.domain.member.repository.SpringDataJpaMemberRepository;
import kyk.SpringFoodWorldProject.domain.member.service.MemberService;
import kyk.SpringFoodWorldProject.domain.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaMemberConfig {
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
