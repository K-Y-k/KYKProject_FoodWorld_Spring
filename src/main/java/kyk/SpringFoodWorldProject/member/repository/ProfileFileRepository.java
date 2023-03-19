package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.domain.entity.ProfileFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 파일 관련 스프링 데이터 JPA 리포지토리
 */
public interface ProfileFileRepository extends JpaRepository<ProfileFile, Long> {

    ProfileFile findByMember(Member member);

    @Modifying
    @Query("update ProfileFile p set p.originalFileName = :originalFileName, p.storedFileName = :storedFileName where p.member.id = :memberId")
    void updateProfileImage(String originalFileName, String storedFileName, Long memberId);

}
