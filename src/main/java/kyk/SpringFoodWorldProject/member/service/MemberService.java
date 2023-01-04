package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    /**
     * 회원 저장
     */
    Member Join(Member member);
    List<Member> findAll();
    Optional<Member> findById(Long memberId);
    Member login(String loginId, String password);

}
