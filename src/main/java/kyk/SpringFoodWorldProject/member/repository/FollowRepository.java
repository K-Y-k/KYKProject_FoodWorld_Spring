package kyk.SpringFoodWorldProject.member.repository;

import kyk.SpringFoodWorldProject.member.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

	@Modifying // INSERT, DELETE, UPDATE 를 네이티브 쿼리로 작성하려면 해당 어노테이션 필요
	@Query(value = "INSERT INTO follow(fromUserId, toUserId) VALUES(:fromUserId, :toUserId)", nativeQuery = true)
	void Follow(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

	@Modifying
	@Query(value = "DELETE FROM follow WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	void UnFollow(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

	@Query(value = "SELECT COUNT(*) FROM follow WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int FollowState(@Param("principalId") int principalId, @Param("pageUserId") int pageUserId);

	@Query(value = "SELECT COUNT(*) FROM follow WHERE fromUserId = :pageUserId", nativeQuery = true)
	int FollowCount(@Param("pageUserId") int pageUserId);

}
