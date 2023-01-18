package kyk.SpringFoodWorldProject.like.repository;

import kyk.SpringFoodWorldProject.like.domain.entity.Like;

import java.util.Optional;

public interface LikeRepository {

    /**
     * 좋아요 엔티티에 저장
     */
    Like save(Like like);

    /**
     * 회원 ID와 게시글 ID 둘다 있는지 검사해서 체크 쿼리 메서드
     */
    Optional<Like> findByMember_IdAndBoard_Id(Long memberId, Long boardId);

    int countByBoard_Id(Long boardId);

    /**
     * 좋아요 엔티티에서 삭제
     */
    void delete(Long likeId);

}
