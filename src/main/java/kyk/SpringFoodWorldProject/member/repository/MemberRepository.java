package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 회원 리포지토리 기본 인터페이스
 */
public interface MemberRepository {

    Member saveMember(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    Member findByName(String name);

    Optional<Member> findByLoginId(String loginId);


    ProfileFile saveProfile(ProfileFile profileFile);

    ProfileFile findProfileByMember(Member member);

    void updateProfileImage(String originalFileName, String storedFileName, Long memberId);

    Page<Member> findPageBy(Pageable pageable);
    Page<Member> findByNameContaining(String memberSearchKeyword, Pageable pageable);

    void delete(Long memberId);

}
