package kyk.SpringFoodWorldProject.board.service;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    /**
     * 글 저장
     */
    Board save(Board board);

    /**
     * 글 수정
     */
    void update(Long boardId, BoardUpdateDto boardUpdateDto);

    /**
     * 글 id로 조회
     */
    Optional<Board> findById(Long id);

    /**
     * 글 모두 조회
     */
    List<Board> findAll();
}
