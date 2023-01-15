package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.dto.JoinForm;
import kyk.SpringFoodWorldProject.member.domain.dto.UpdateForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface MemberService {

    /**
     * 회원 저장
     */
    Long join(JoinForm form);

    /**
     * 회원 로그인
     */
    Member login(String loginId, String password);


    /**
     * 회원 id로 조회
     */
    Optional<Member> findById(Long memberId);

    /**
     * 회원 모두 조회
     */
    List<Member> findAll();


}
