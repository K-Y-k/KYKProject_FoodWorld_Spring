package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Override
    public Member Join(Member member) {
        return memberRepository.save(member);
    }


    /**
     * 전체회원조회
     */
    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    /**
     * 회원 찾기
     */
    @Override
    public Optional<Member> findById(Long memberId) {  // 받아온 id의 맴버 찾기
        return memberRepository.findById(memberId);
    }


    /**
     * 로그인 기능
     */
    @Override
    public Member login(String loginId, String password) {        // 로그인 실패 시 null로 반환
        // 람다를 활용하여 위 코드를 축약한 것
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}


