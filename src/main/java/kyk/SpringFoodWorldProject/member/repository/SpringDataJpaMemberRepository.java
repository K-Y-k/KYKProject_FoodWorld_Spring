package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 * 스프링 데이터 JPA로 구현한 회원 리포지토리
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SpringDataJpaMemberRepository implements MemberRepository {
    private final JPAMemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member findByName(String name) {
        return memberRepository.findByName(name);
    }


    /**
     * 로그인 id로 상세조회
     */
    // 못 찾을 수 있기에 Optional로 감싸준다.
    public Optional<Member> findByLoginId(String loginId) {
        // 람다를 사용해서 위 코드를 축약한 것 (최근의 기본 형태)
        // 리스트를 stream으로 바꾸고 루프를 돌고 필터의 조건에 만족할 경우에만 다음 단계로 넘어간다.
        // 여기서 다음 단계는 findFirst(처음에 나온 것을 반환)이다.
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    public void clear() {
        memberRepository.deleteAll();
    }

}
