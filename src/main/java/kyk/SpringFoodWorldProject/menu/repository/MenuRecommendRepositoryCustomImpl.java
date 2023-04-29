package kyk.SpringFoodWorldProject.menu.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.BoardRepositoryCustom;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuSearchCond;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.domain.entity.QMenuRecommend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static kyk.SpringFoodWorldProject.board.domain.entity.QBoard.board;
import static kyk.SpringFoodWorldProject.board.domain.entity.QBoardFile.boardFile;
import static kyk.SpringFoodWorldProject.member.domain.entity.QMember.member;
import static kyk.SpringFoodWorldProject.menu.domain.entity.QMenuRecommend.menuRecommend;
import static org.springframework.util.StringUtils.hasText;

/**
 * 사용자 정의 인터페이스를 상속받은 사용자 정의 리포지토리
 */
@Slf4j
public class MenuRecommendRepositoryCustomImpl implements MenuRecommendRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public MenuRecommendRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); // 이렇게 JPAQueryFactory를 사용할 수는 있다.
    }

    @Override
    public Page<MenuRecommend> categoryMenuList(MenuSearchCond menuSearchCond, Pageable pageable) {
        QueryResults<MenuRecommend> results = queryFactory.selectFrom(menuRecommend)
                .leftJoin(menuRecommend.member, member)
                .where(
                        menuCategoryEq(menuSearchCond.getSelectedCategory()),
                        menuNameEq(menuSearchCond.getMenuNameKeyword()),
                        franchisesEq(menuSearchCond.getFranchisesKeyword())
                )
                .orderBy(menuRecommend.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MenuRecommend> content = results.getResults();   // .getResults()는 조회한 데이터를 가져옴
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);      // PageImpl<>로 반환해야한다.
    }

    private BooleanExpression menuCategoryEq(String selectedCategory) {
        return hasText(selectedCategory) ? menuRecommend.category.eq(selectedCategory) : null;
    }
    private BooleanExpression menuNameEq(String getMenuNameKeyword) {
        return hasText(getMenuNameKeyword) ? menuRecommend.menuName.like("%" + getMenuNameKeyword + "%"): null;
    }
    private BooleanExpression franchisesEq(String getFranchisesKeyword) {
        return hasText(getFranchisesKeyword) ? menuRecommend.franchises.like("%" + getFranchisesKeyword + "%") : null;
    }

}
