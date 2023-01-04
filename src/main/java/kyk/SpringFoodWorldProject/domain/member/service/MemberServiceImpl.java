package kyk.SpringFoodWorldProject.domain.member.service;

import kyk.SpringFoodWorldProject.domain.member.entity.Member;
import kyk.SpringFoodWorldProject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }
}
