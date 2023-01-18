package kyk.SpringFoodWorldProject.like.service;

import kyk.SpringFoodWorldProject.like.domain.entity.Like;

import java.util.Optional;

public interface LikeService {

    /**
     *
     */
    int saveLike(Long memberId, Long boardId);

    /**
     * 게시글의 id에 맞는 좋아요 총 개수 파악
     */
    int countByBoard_Id(Long boardId);

}
