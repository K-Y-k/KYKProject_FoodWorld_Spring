package kyk.SpringFoodWorldProject.member.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.follow.domain.dto.FollowDto;
import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {

    private final BoardServiceImpl boardService;
    private final FollowServiceImpl followService;
    private final MemberServiceImpl memberService;



    /**
     * 회원 닉네임 중복 검사
     */
    @GetMapping("/api/checkName")
    public ResponseEntity<?> checkName(@RequestParam(value = "memberName") String memberName,
                                       @RequestParam(value = "getMemberId") Long memberId) {
        log.info("memberId={}", memberId);
        int result;

        if (memberId == 0) { // 회원가입중에 중복검사인 경우
            result = memberService.checkName(memberName);
        } else { // 프로필 수정에서 중복검사인 경우
            result = memberService.updateCheckName(memberName, memberId);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 회원 로그인 아이디 중복 검사
     */
    @GetMapping("/api/checkLoginId")
    public ResponseEntity<?> checkLoginId(@RequestParam(value = "memberLoginId") String memberLoginId,
                                          @RequestParam(value = "getMemberId") Long memberId) {
        log.info("memberId={}", memberId);
        int result;

        if (memberId == 0) { // 회원가입중에 중복검사인 경우
            result = memberService.checkLoginId(memberLoginId);
        } else { // 프로필 수정에서 중복검사인 경우
            result = memberService.updateCheckLoginId(memberLoginId, memberId);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    /**
     * 현재 회원 프로필의 회원의 팔로워 조회
     */
    @GetMapping("/api/follow/{toMemberId}")
    public ResponseEntity<?> followerScroll(@PathVariable Long toMemberId,
                                            @RequestParam(value = "lastCursorFollowerId", defaultValue = "0") Long lastCursorFollowerId,
                                            @RequestParam(value = "followerFirst") Boolean first,
                                            @PageableDefault(size=7) Pageable pageable) {

        Member findToMember = memberService.findById(toMemberId).orElseThrow(() ->
                new IllegalArgumentException("회원 가져오기 실패: 회원을 찾지 못했습니다." + toMemberId));

        Slice<FollowDto> followers = followService.searchBySlice(findToMember, lastCursorFollowerId, first, pageable);

        for (FollowDto follower : followers) {
            log.info("follower={}", follower.getFromMember().getName());
        }
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }


    /**
     * 현재 회원과 연관된 팔로워 추천
     */
//    @GetMapping("/api/followRecommend")
//    public ResponseEntity<?> recommendFollower(@RequestParam(value = "userId") Long userId) {
//        if (userId != null) {
//            List<Member> recommendMember = followService.recommendMember(userId);
//            return new ResponseEntity<>(recommendMember, HttpStatus.OK);
//        }
//        else {
//            return ResponseEntity.ok(HttpStatus.OK);
//        }
//    }


    /**
     * 현재 클릭한 프로필의 회원이 작성한 먹스타그램 글을 ajax 비동기로 받은 마지막 id를 기준으로 json 변형후 보내줌
     */
    @GetMapping("/api/muckstarBoard")
    public ResponseEntity<?> muckstarBoardsScroll(@RequestParam(value = "lastCursorBoardId", defaultValue = "0") Long lastCursorBoardId,
                                                  @RequestParam(value = "memberId") String memberId,
                                                  @RequestParam(value = "muckstarFirst") Boolean first,
                                                  @PageableDefault(size=27) Pageable pageable) {
        String boardType = "먹스타그램";
        log.info("회원 아이디 = {}", memberId);

        Slice<Board> boards = boardService.searchBySlice(memberId, lastCursorBoardId, first, pageable, boardType);

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

}
