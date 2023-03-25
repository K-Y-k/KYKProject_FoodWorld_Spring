package kyk.SpringFoodWorldProject.menu.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MenuRecommendRepository {

    MenuRecommend save(MenuRecommend menuRecommend);

    Optional<MenuRecommend> findById(Long menuRecommendId);

    Page<MenuRecommend> findByMemberId(Pageable pageable, Long memberId);

}
