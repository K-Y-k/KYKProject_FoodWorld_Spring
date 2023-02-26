package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * QueryDsl을 구현하기 위한 사용자 정의 인터페이스
 */
public interface BoardRepositoryCustom {
    Long findFirstCursorBoardId(String boardType);
    Slice<Board> searchBySlice(Long lastCursorBoardId, BoardSearchCond boardSearchCond, Pageable pageable, String boardType);
}
