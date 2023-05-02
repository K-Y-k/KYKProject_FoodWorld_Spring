package kyk.SpringFoodWorldProject.board.repository;

import kyk.SpringFoodWorldProject.admin.dto.AdminBoardDTO;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

/**
 * QueryDsl을 구현하기 위한 사용자 정의 인터페이스
 */
public interface BoardRepositoryCustom {
    List<Board> findByMemberId(Long memberId);
    Long findFirstCursorBoardId(String boardType);
    Long findFirstCursorBoardIdInMember(Long memberId, String boardType);
    Slice<Board> searchBySlice(String memberId, Long lastCursorBoardId, Boolean first, Pageable pageable, String boardType);
    Slice<Board> searchBySliceByWriter(String memberId, Long lastCursorBoardId, Boolean first, String writerSearchKeyword, Pageable pageable, String boardType);

    List<Board> popularBoardList(String boardType);

    Page<Board> categoryBoardList(String boardType, BoardSearchCond boardSearchDto, Pageable pageable);
}
