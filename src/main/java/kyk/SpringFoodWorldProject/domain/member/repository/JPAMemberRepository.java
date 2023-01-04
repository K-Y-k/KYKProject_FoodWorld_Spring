package kyk.SpringFoodWorldProject.domain.member.repository;

import kyk.SpringFoodWorldProject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * SpringDataJpa를 이용한 리포지토리 인터페이스
 *  구현체 없이 기본적인 CRUD 기능 및 공통 기술 제공
 *  메서드 이름과 관련된 기능 파악해서 자동 제공
 */
public interface JPAMemberRepository extends JpaRepository<Member, Long> {

}
