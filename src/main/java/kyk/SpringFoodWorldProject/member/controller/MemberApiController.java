package kyk.SpringFoodWorldProject.member.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {
    private final BoardServiceImpl boardService;

    /**
     * ajax 비동기로 받은 마지막 id를 기준으로 json 변형후 보내줌
     */
    @GetMapping("/api/muckstarBoard")
    public ResponseEntity<?> muckstarBoardsScroll(@RequestParam(value = "lastCursorBoardId", defaultValue = "0") Long lastCursorBoardId,
                                                  @RequestParam(value = "memberId") String memberId,
                                                  @RequestParam(value = "first") Boolean first,
                                                  @PageableDefault(size=9) Pageable pageable,
                                                  BoardSearchCond boardSearchCond) {
        String boardType = "먹스타그램";
        log.info("회원 아이디 = {}", memberId);

        Slice<Board> boards = boardService.searchBySlice(memberId, lastCursorBoardId, first, boardSearchCond, pageable, boardType);

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }
}
