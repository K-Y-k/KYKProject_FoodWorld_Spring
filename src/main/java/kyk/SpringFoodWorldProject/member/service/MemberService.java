package kyk.SpringFoodWorldProject.member.service;

import kyk.SpringFoodWorldProject.member.domain.dto.JoinForm;
import kyk.SpringFoodWorldProject.member.domain.dto.UpdateForm;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    Long changeProfile(Long memberId, UpdateForm form) throws IOException;

    Page<Member> findPageBy(Pageable pageable);
    Page<Member> findByNameContaining(String memberSearchKeyword, Pageable pageable);

    void delete(Long memberId);

    int checkName(String memberName);
    int checkLoginId(String memberLoginId);
    int updateCheckName(String memberName, Long memberId);
    int updateCheckLoginId(String memberLoginId, Long memberId);


}
