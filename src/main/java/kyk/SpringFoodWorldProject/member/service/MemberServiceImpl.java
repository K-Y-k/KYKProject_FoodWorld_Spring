package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.dto.JoinForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    /**
     * 회원가입
     */
    @Override
    public Long join(JoinForm form) {
        Member memberEntity = form.toEntity();

        // 중복 회원 검증 로직
        validateDuplicateMember(memberEntity);

        Member savedMember = memberRepository.save(memberEntity);
        return savedMember.getId();
    }

    private void validateDuplicateMember(Member memberEntity) {
        // 문제가 있으면 EXCEPTION
        // 같은 로그인 아이디가 있는지 찾음
        // 반환형태가 Optional<Member>이므로 이렇게 바로 작성가능
        memberRepository.findByLoginId(memberEntity.getLoginId())
                .ifPresent(m-> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
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
    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

}


