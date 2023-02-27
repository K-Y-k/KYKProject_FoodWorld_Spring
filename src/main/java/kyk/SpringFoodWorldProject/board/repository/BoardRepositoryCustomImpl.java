package kyk.SpringFoodWorldProject.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.QBoard;
import kyk.SpringFoodWorldProject.board.domain.entity.QBoardFile;
import kyk.SpringFoodWorldProject.member.domain.entity.QMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;
import java.util.List;

import static kyk.SpringFoodWorldProject.board.domain.entity.QBoard.board;
import static kyk.SpringFoodWorldProject.board.domain.entity.QBoardFile.boardFile;
import static kyk.SpringFoodWorldProject.member.domain.entity.QMember.member;

/**
 * 사용자 정의 인터페이스를 상속받은 사용자 정의 리포지토리
 */
@Slf4j
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    public BoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); // 이렇게 JPAQueryFactory를 사용할 수는 있다.
    }

    public Long findFirstCursorBoardId(String boardType) {
        Board findBoard = queryFactory.selectFrom(board)
                .where(
                        board.boardType.eq(boardType)
                )
                .limit(1)
                .orderBy(board.id.desc())
                .fetchOne();

        return findBoard.getId();
    }


    @Override
    public Slice<Board> searchBySlice(Long lastCursorBoardId, BoardSearchCond boardSearchCond, Pageable pageable, String boardType) {

        List<Board> results = queryFactory.selectFrom(board)
                .leftJoin(board.boardFiles, boardFile).fetchJoin()
                .where(
                        ltBoardId(lastCursorBoardId),
                        board.boardType.eq(boardType)
                )
                .limit(pageable.getPageSize() + 1)
                .orderBy(board.id.desc())
                .fetch();

        log.info("리스트 개수=", results.size());

        return checkLastPage(pageable, results);
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltBoardId(Long lastCursorBoardId) {
        if (lastCursorBoardId == null) {
            return null;
        }
        return board.id.loe(lastCursorBoardId);
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<Board> checkLastPage(Pageable pageable, List<Board> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
