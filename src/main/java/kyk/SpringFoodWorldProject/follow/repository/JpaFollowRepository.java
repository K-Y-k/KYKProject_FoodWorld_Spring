package kyk.SpringFoodWorldProject.follow.repository;

import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaFollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

	/**
	 * 현재 회원이 상대 회원을 팔로우한 상태인지 확인
	 */
	Optional<Follow> findByFromMember_IdAndToMember_Id(Long memberId, Long boardId);

	/**
	 * 팔로워 수 조회
	 */
	int countByToMember_Id(Long toMemberId);


	/**
	 * 팔로잉 수 조회
	 */
	int countByFromMember_Id(Long fromMemberId);


	/**
	 * 팔로우 해제
	 */
	void deleteByFromMember_IdAndToMember_Id(Long fromMemberId, Long toMemberId);

	@Query(value = "SELECT * " +
			"FROM ( " +
			"  SELECT DISTINCT m.*, ROW_NUMBER() OVER (ORDER BY dbms_random.value) AS rn " +
			"  FROM ( " +
			"    SELECT f1.fromMember_id AS member_id " +
			"    FROM Follow f1 " +
			"    WHERE f1.toMember_id = :currentMemberId " +
			"    UNION " +
			"    SELECT f2.toMember_id AS member_id " +
			"    FROM Follow f2 " +
			"    WHERE f2.fromMember_id = :currentMemberId " +
			"  ) f " +
			"  INNER JOIN Member m ON m.member_id = f.member_id " +
			") " +
			"WHERE rn <= 5;", nativeQuery = true)
	List<Member> recommendFollow(Long currentMemberId);
}
