package kyk.SpringFoodWorldProject.follow.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.follow.domain.entity.QFollow;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;

import java.util.List;

import static kyk.SpringFoodWorldProject.board.domain.entity.QBoard.board;
import static kyk.SpringFoodWorldProject.board.domain.entity.QBoardFile.boardFile;
import static kyk.SpringFoodWorldProject.follow.domain.entity.QFollow.follow;
import static kyk.SpringFoodWorldProject.member.domain.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public FollowRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Long findFirstCursorFollowerId(Member member) {
        Follow findFollower = queryFactory.selectFrom(follow)
                .where(
                       follow.toMember.eq(member)
                )
                .limit(1)
                .orderBy(follow.id.desc())
                .fetchOne();

        return findFollower.getId();
    }

    @Override
    public Slice<Follow> searchBySlice(Member member, Long lastCursorFollowerId, Boolean first, Pageable pageable) {

        List<Follow> results = queryFactory.selectFrom(follow)
                .leftJoin(follow.fromMember).fetchJoin()
                .leftJoin(follow.toMember).fetchJoin()
                .where(
                        ltFollowerId(lastCursorFollowerId, first),
                        follow.toMember.eq(member)
                )
                .limit(pageable.getPageSize() + 1)
                .orderBy(follow.id.desc())
                .fetch();

        log.info("팔로워 리스트 ={}", results.size());
        return checkLastPage(pageable, results);
    }

    // BooleanExpression으로 하면 조합 가능해진다.
    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltFollowerId(Long lastCursorFollowerId, Boolean first) {
        if (lastCursorFollowerId == null) {
            return null;
        }
        else if (first) {
            return follow.id.loe(lastCursorFollowerId);
        }
        else {
            return follow.id.lt(lastCursorFollowerId);
        }
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<Follow> checkLastPage(Pageable pageable, List<Follow> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
