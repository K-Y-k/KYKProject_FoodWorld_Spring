package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    /**
     * 회원 저장
     */
    Member Join(Member member);

    /**
     * 회원 id로 조회
     */
    Optional<Member> findById(Long memberId);
    
    /**
     * 회원 모두 조회
     */
    List<Member> findAll();

    /**
     * 회원 로그인
     */
    Member login(String loginId, String password);

}
