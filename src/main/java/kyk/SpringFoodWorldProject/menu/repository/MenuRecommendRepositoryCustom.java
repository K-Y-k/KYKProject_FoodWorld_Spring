package kyk.SpringFoodWorldProject.menu.repository;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuSearchCond;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * QueryDsl을 구현하기 위한 사용자 정의 인터페이스
 */
public interface MenuRecommendRepositoryCustom {
    Page<MenuRecommend> categoryMenuList(MenuSearchCond menuSearchCond, Pageable pageable);
}
