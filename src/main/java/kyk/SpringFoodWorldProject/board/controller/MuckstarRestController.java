package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class MuckstarRestController {

    private final BoardServiceImpl boardService;

    /**
     * ajax 비동기로 받은 마지막 id를 기준으로 json 변형후 보내줌
     */
    @GetMapping("/api/muckstarBoard")
    public ResponseEntity<?> muckstarBoardsScroll(@RequestParam(value = "lastCursorBoardId", defaultValue = "0") Long lastCursorBoardId,
                                                  @PageableDefault(size=3) Pageable pageable,
                                                  BoardSearchCond boardSearchCond,
                                                  Model model) {
        String boardType = "먹스타그램";

        Slice<Board> boards = boardService.searchBySlice(lastCursorBoardId, boardSearchCond, pageable, boardType);

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }
}
