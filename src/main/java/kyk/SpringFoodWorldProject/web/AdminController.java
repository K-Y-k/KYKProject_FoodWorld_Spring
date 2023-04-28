package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberServiceImpl memberService;
    private final BoardServiceImpl boardService;

    @GetMapping("/freeBoard")
    public String main(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model,
                       BoardSearchCond boardSearchCond) {
//        if (loginMember == null) {
//            log.info("로그인 상태가 아님");
//
//            model.addAttribute("message", "로그인 먼저 해주세요!");
//            model.addAttribute("redirectUrl", "/members/login");
//            return "messages";
//        } else if (!loginMember.getRole().equals("admin")) {
//            log.info("관리자님이 아닙니다.");
//
//            model.addAttribute("message", "관리자님이 아닙니다.");
//            model.addAttribute("redirectUrl", "/");
//            return "messages";
//        }

        Page<Board> boards;
        String boardType = "자유게시판";

        String writerSearchKeyword = boardSearchCond.getWriterSearchKeyword();
        String titleSearchKeyword = boardSearchCond.getTitleSearchKeyword();

        // 키워드의 컬럼에 따른 페이징된 게시글 출력
        if (writerSearchKeyword == null && titleSearchKeyword == null) {
            boards = boardService.findPageListByBoardType(pageable, boardType);
        } else if (writerSearchKeyword.isEmpty()) {
            boards = boardService.findByTitleContaining(titleSearchKeyword, pageable, boardType);
        } else if (titleSearchKeyword.isEmpty()) {
            boards = boardService.findByWriterContaining(writerSearchKeyword, pageable, boardType);
        } else {
            boards = boardService.findByTitleContainingAndWriterContaining(titleSearchKeyword, writerSearchKeyword, pageable, boardType);
        }

        // 댓글 개수
        for (Board board : boards) {
            Board findBoard = boardService.findById(board.getId()).orElseThrow(() ->
                    new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + board.getId()));
            boardService.updateCommentCount(findBoard.getId());
            boardService.updateCount(board.getId());
        }

        int nowPage = pageable.getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 2);
        int endPage = Math.min(nowPage + 2, boards.getTotalPages());

        model.addAttribute("boards", boards);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("localDateTime", LocalDateTime.now());

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        model.addAttribute("hasPrev", boards.hasPrevious());
        model.addAttribute("hasNext", boards.hasNext());

        model.addAttribute("writerSearchKeyword", boardSearchCond.getWriterSearchKeyword());
        model.addAttribute("titleSearchKeyword", boardSearchCond.getTitleSearchKeyword());

        return "admin/admin_freeBoard";
    }


    @GetMapping("/boards/delete/{boardType}/{boardId}")
    public String boardDelete(@PathVariable String boardType,
                              @PathVariable Long boardId,
                              @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (boardType.equals("자유게시판")) {
            boardType = "freeBoard";
        } else if (boardType.equals("추천게시판")) {
            boardType = "recommendBoard";
        } else {
            boardType = "muckstarBoard";
        }

        if (loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        } else if (loginMember.getRole().equals("admin")) {
            boardService.delete(boardId);
        } else {
            log.info("관리자님이 아닙니다.");

            model.addAttribute("message", "관리자님이 아닙니다.");
            model.addAttribute("redirectUrl", "/");
            return "messages";
        }

        model.addAttribute("message", "삭제 성공");
        redirectAttributes.addAttribute("boardId", boardId);
        model.addAttribute("redirectUrl", "/admin/"+boardType);
        return "messages";
    }
}
