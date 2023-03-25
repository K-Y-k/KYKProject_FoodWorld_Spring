package kyk.SpringFoodWorldProject.menu.repository;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JPAMenuRecommendRepository extends JpaRepository<MenuRecommend, Long> {

    Page<MenuRecommend> findPageListByMemberId(Pageable pageable, Long memberId);

}
