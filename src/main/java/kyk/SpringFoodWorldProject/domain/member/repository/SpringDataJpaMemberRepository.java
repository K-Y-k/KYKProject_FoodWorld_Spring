package kyk.SpringFoodWorldProject.domain.member.repository;

import kyk.SpringFoodWorldProject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class SpringDataJpaMemberRepository implements MemberRepository {

    private final JPAMemberRepository repository;


    @Override
    public Member save(Member member) {
        return repository.save(member);
    }


}
