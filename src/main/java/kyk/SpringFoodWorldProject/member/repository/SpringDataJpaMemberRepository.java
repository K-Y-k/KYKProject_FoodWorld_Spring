package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 스프링 데이터 JPA로 구현한 회원 리포지토리
 */
@Slf4j
@Repository
@Transactional // JPA의 모든 데이터 변경은 트랜잭션에서 일어나기에 있어야한다.
@RequiredArgsConstructor
public class SpringDataJpaMemberRepository implements MemberRepository {

    private final JPAMemberRepository repository;

    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Member findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }


    /**
     * 로그인 id로 상세조회
     */
    // 못 찾을 수 있기에 Optional로 감싸준다.
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }


}
