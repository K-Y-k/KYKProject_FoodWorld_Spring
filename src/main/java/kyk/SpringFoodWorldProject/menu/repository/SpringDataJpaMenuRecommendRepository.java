package kyk.SpringFoodWorldProject.menu.repository;

import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaMenuRecommendRepository implements MenuRecommendRepository {

    private final JPAMenuRecommendRepository menuRecommendRepository;


    @Override
    public MenuRecommend save(MenuRecommend menuRecommend) {
        return menuRecommendRepository.save(menuRecommend);
    }

    @Override
    public Optional<MenuRecommend> findById(Long menuRecommendId) {
        return menuRecommendRepository.findById(menuRecommendId);
    }

    @Override
    public Page<MenuRecommend> findByMemberId(Pageable pageable, Long memberId) {
        return menuRecommendRepository.findPageListByMemberId(pageable, memberId);
    }
}
