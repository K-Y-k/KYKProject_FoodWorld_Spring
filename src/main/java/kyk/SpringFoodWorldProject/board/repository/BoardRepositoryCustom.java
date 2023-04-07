package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

/**
 * QueryDsl을 구현하기 위한 사용자 정의 인터페이스
 */
public interface BoardRepositoryCustom {
    Long findFirstCursorBoardId(String boardType);
    Slice<Board> searchBySlice(String memberId, Long lastCursorBoardId, Boolean first, BoardSearchCond boardSearchCond, Pageable pageable, String boardType);

    Long findFirstCursorBoardIdInMember(Long memberId, String boardType);

    List<Board> popularBoardList(String boardType);
}
