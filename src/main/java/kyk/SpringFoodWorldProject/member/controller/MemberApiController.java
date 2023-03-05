package kyk.SpringFoodWorldProject.member.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.follow.service.FollowServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {
    private final BoardServiceImpl boardService;
    private final FollowServiceImpl followService;

    /**
     * ajax 비동기로 받은 마지막 id를 기준으로 json 변형후 보내줌
     */
    @GetMapping("/api/muckstarBoard")
    public ResponseEntity<?> muckstarBoardsScroll(@RequestParam(value = "lastCursorBoardId", defaultValue = "0") Long lastCursorBoardId,
                                                  @RequestParam(value = "memberId") String memberId,
                                                  @RequestParam(value = "first") Boolean first,
                                                  @PageableDefault(size=27) Pageable pageable,
                                                  BoardSearchCond boardSearchCond) {
        String boardType = "먹스타그램";
        log.info("회원 아이디 = {}", memberId);

        Slice<Board> boards = boardService.searchBySlice(memberId, lastCursorBoardId, first, boardSearchCond, pageable, boardType);

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }


//    /**
//     * 팔로우
//     */
//    @GetMapping("/api/follow/{toUserId}")
//    public ResponseEntity<?> subscribe(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER) Member loginMember,
//                                       @PathVariable Long toUserId){
//        followService.followAndUnFollow(loginMember.getId(), toUserId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
