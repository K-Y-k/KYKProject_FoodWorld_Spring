package kyk.SpringFoodWorldProject.like.repository;

import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JPALikeRepository extends JpaRepository<Like, Long> {

    /**
     * 회원 ID와 게시글 ID 둘다 있는지 검사해서 체크 쿼리 메서드
     */
    Optional<Like> findByMember_IdAndBoard_Id(Long memberId, Long boardId);


    /**
     * 해당 게시글의 좋아요 개수를 카운팅하기 위한 쿼리 메서드
     */
    int countByBoard_Id(Long boardId);

}
