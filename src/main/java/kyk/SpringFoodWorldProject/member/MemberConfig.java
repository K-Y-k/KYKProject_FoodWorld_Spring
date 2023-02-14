//package kyk.SpringFoodWorldProject.member;
//
//import kyk.SpringFoodWorldProject.member.repository.JPAMemberRepository;
//import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
//import kyk.SpringFoodWorldProject.member.repository.SpringDataJpaMemberRepository;
//import kyk.SpringFoodWorldProject.member.service.MemberService;
//import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 스프링 데이터 JPA로 구현한 회원 리포지토리 수동 빈 설정
// */
//@Configuration
//@RequiredArgsConstructor
//public class MemberConfig {
//    private final JPAMemberRepository repository;
//
//
//    // 빈이 서로에 대해 종속성을 가질 때에는 아래 코드와 같이 해당 종속성을 표현하는 것이다.
//    // 종속성을 표현하는 방법은 하나의 빈 메소드가 다른 메소드를 호출하도록 하는 것이다.
//    @Bean
//    public MemberService memberService() {
//        return new MemberServiceImpl(memberRepository());
//    }
//
//    @Bean
//    public MemberRepository memberRepository() {
//        return new SpringDataJpaMemberRepository(repository);
//    }
//
//}
