package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

	@Modifying // INSERT, DELETE, UPDATE를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요
	@Query(value = "INSERT INTO follow(fromUserId, toUserId) VALUES(:fromUserId, :toUserId)", nativeQuery = true)
	void follow(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

	@Modifying
	@Query(value = "DELETE FROM follow WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	void unFollow(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

	@Query(value = "SELECT COUNT(*) FROM follow WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int followState(@Param("principalId") Long principalId, @Param("pageUserId") Long pageUserId);

	@Query(value = "SELECT COUNT(*) FROM follow WHERE fromUserId = :pageUserId", nativeQuery = true)
	int followCount(@Param("pageUserId") Long pageUserId);

}
