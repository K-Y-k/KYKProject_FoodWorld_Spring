package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.chat.domain.dto.ChatSearchCond;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.chat.service.ChatService;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.dto.MemberSearchCond;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuSearchCond;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.service.MenuRecommendServiceImpl;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberServiceImpl memberService;
    private final BoardServiceImpl boardService;
    private final MenuRecommendServiceImpl menuRecommendService;
    private final ChatService chatService;

    @GetMapping("/members")
    public String adminMember(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                              Model model,
                              MemberSearchCond memberSearchCond) {
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

        Page<Member> members;

        // 키워드의 컬럼에 따른 페이징된 회원 출력
        String memberSearchKeyword = memberSearchCond.getMemberSearchKeyword();
        log.info("memberSearchKeyword = {}", memberSearchKeyword);

        if (memberSearchKeyword == null) {
            members = memberService.findPageBy(pageable);
        } else {
            members = memberService.findByNameContaining(memberSearchKeyword, pageable);
        }

        int nowPage = pageable.getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 2);
        int endPage = Math.min(nowPage + 2, members.getTotalPages());

        model.addAttribute("members", members);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("localDateTime", LocalDateTime.now());

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        model.addAttribute("hasPrev", members.hasPrevious());
        model.addAttribute("hasNext", members.hasNext());

        model.addAttribute("memberSearchKeyword", memberSearchKeyword);

        return "admin/admin_member";
    }

    @GetMapping("/member/delete/{memberId}")
    public String memberDelete(@PathVariable Long memberId,
                               @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                               Model model) {
        if (loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        } else if (loginMember.getRole().equals("admin")) {
            memberService.delete(memberId);
        } else {
            log.info("관리자님이 아닙니다.");

            model.addAttribute("message", "관리자님이 아닙니다.");
            model.addAttribute("redirectUrl", "/");
            return "messages";
        }

        model.addAttribute("message", "회원 추방 성공");
        model.addAttribute("redirectUrl", "/admin/members");
        return "messages";
    }


    @GetMapping("/freeBoard")
    public String adminFreeBoard(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
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
        boards = searchKeyWord(pageable, boardType, writerSearchKeyword, titleSearchKeyword);

        // 댓글 개수
        boardCommentCount(boards);

        pagingModel(pageable, model, boardSearchCond, boards);

        return "admin/admin_freeBoard";
    }

    @GetMapping("/recommendBoard")
    public String adminRecommendBoard(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
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
        String boardType = "추천게시판";

        String writerSearchKeyword = boardSearchCond.getWriterSearchKeyword();
        String titleSearchKeyword = boardSearchCond.getTitleSearchKeyword();

        // 키워드의 컬럼에 따른 페이징된 게시글 출력
        boards = searchKeyWord(pageable, boardType, writerSearchKeyword, titleSearchKeyword);

        // 댓글 개수
        boardCommentCount(boards);

        pagingModel(pageable, model, boardSearchCond, boards);

        return "admin/admin_recommendBoard";
    }

    @GetMapping("/muckstarBoard")
    public String adminMuckstarBoard(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
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
        String boardType = "먹스타그램";

        String writerSearchKeyword = boardSearchCond.getWriterSearchKeyword();

        // 키워드의 컬럼에 따른 페이징된 게시글 출력
        if (writerSearchKeyword == null) {
            boards = boardService.findPageListByBoardType(pageable, boardType);
        } else {
            boards = boardService.findByWriterContaining(writerSearchKeyword, pageable, boardType);
        }

        // 댓글 개수
        boardCommentCount(boards);

        pagingModel(pageable, model, boardSearchCond, boards);

        return "admin/admin_muckstarBoard";
    }

    private Page<Board> searchKeyWord(Pageable pageable, String boardType, String writerSearchKeyword, String titleSearchKeyword) {
        Page<Board> boards;
        if (writerSearchKeyword == null && titleSearchKeyword == null) {
            boards = boardService.findPageListByBoardType(pageable, boardType);
        } else if (writerSearchKeyword.isEmpty()) {
            boards = boardService.findByTitleContaining(titleSearchKeyword, pageable, boardType);
        } else if (titleSearchKeyword.isEmpty()) {
            boards = boardService.findByWriterContaining(writerSearchKeyword, pageable, boardType);
        } else {
            boards = boardService.findByTitleContainingAndWriterContaining(titleSearchKeyword, writerSearchKeyword, pageable, boardType);
        }
        return boards;
    }

    private void boardCommentCount(Page<Board> boards) {
        for (Board board : boards) {
            Board findBoard = boardService.findById(board.getId()).orElseThrow(() ->
                    new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + board.getId()));
            boardService.updateCommentCount(findBoard.getId());
            boardService.updateCount(board.getId());
        }
    }

    private void pagingModel(Pageable pageable, Model model, BoardSearchCond boardSearchCond, Page<Board> boards) {
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
    }


    @GetMapping("/boards/delete/{boardType}/{boardId}")
    public String boardDelete(@PathVariable String boardType,
                              @PathVariable Long boardId,
                              @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        switch (boardType) {
            case "자유게시판":
                boardType = "freeBoard";
                break;
            case "추천게시판":
                boardType = "recommendBoard";
                break;
            case "먹스타그램":
                boardType = "muckstarBoard";
                break;
        }

        if (loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        } else if (loginMember.getRole().equals("admin")) {
            boardService.delete(boardId, boardType);
        } else {
            log.info("관리자님이 아닙니다.");

            model.addAttribute("message", "관리자님이 아닙니다.");
            model.addAttribute("redirectUrl", "/");
            return "messages";
        }

        model.addAttribute("message", "게시글 삭제 성공");
        redirectAttributes.addAttribute("boardId", boardId);
        model.addAttribute("redirectUrl", "/admin/"+boardType);
        return "messages";
    }


    @GetMapping("/menu")
    public String adminMenu(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            Model model,
                            MenuSearchCond menuSearchCond) {
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

        log.info("선택된 카테고리 = {}", menuSearchCond.getSelectedCategory());

        // 키워드의 컬럼에 따른 페이징된 메뉴 출력
        Page<MenuRecommend> menuRecommends = menuRecommendService.categoryMenuList(menuSearchCond, pageable);

        int nowPage = pageable.getPageNumber() + 1;  // 페이지에 넘어온 페이지를 가져옴 == boards.getPageable().getPageNumber()
                                                     // pageable의 index는 0부터 시작이기에 1을 더해준 것이다.

        int startPage = Math.max(1, nowPage - 2);                            // 마이너스가  나오지 않게 Math.max로 최대 1로 조정
        int endPage = Math.min(nowPage + 2, menuRecommends.getTotalPages()); // 총 페이지보다 넘지 않게 Math.min으로 조정


        // 페이징된 게시글 모델과 시작/현재/끝페이지 모델
        model.addAttribute("menuRecommends", menuRecommends);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 등록한 날이 오늘 날짜이면 시/분까지만 나타나게 조건을 설정하기 위해서 현재 시간을 객체로 담아 보낸 것
        model.addAttribute("localDateTime", LocalDateTime.now());

        // 이전, 다음으로 적용
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        // 이전, 다음 페이지의 존재 여부
        model.addAttribute("hasPrev", menuRecommends.hasPrevious());
        model.addAttribute("hasNext", menuRecommends.hasNext());

        // 검색된 파라미터 모델
        model.addAttribute("menuNameKeyword", menuSearchCond.getMenuNameKeyword());
        model.addAttribute("franchisesKeyword", menuSearchCond.getFranchisesKeyword());

        return "admin/admin_menu";
    }

    @GetMapping("/menu/delete/{menuRecommendId}")
    public String menuDelete(@PathVariable Long menuRecommendId,
                             @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             Model model) throws IOException {
        if (loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        } else if (loginMember.getRole().equals("admin")) {
            menuRecommendService.delete(menuRecommendId);
        } else {
            log.info("관리자님이 아닙니다.");

            model.addAttribute("message", "관리자님이 아닙니다.");
            model.addAttribute("redirectUrl", "/");
            return "messages";
        }

        model.addAttribute("message", "메뉴 삭제 성공");
        model.addAttribute("redirectUrl", "/admin/menu");
        return "messages";
    }


    @GetMapping("/chat")
    public String adminChatRoom(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model,
                                ChatSearchCond chatSearchCond) {
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


        // 키워드의 컬럼에 따른 페이징된 게시글 출력
        Page<ChatRoom> chatRooms = chatService.searchChatRoomByMember(chatSearchCond.getMemberSearchKeyword(), pageable);

        // 패이지
        int nowPage = pageable.getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 2);
        int endPage = Math.min(nowPage + 2, chatRooms.getTotalPages());

        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("localDateTime", LocalDateTime.now());

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        model.addAttribute("hasPrev", chatRooms.hasPrevious());
        model.addAttribute("hasNext", chatRooms.hasNext());

        model.addAttribute("memberSearchKeyword", chatSearchCond.getMemberSearchKeyword());

        return "admin/admin_chat";
    }

    @GetMapping("/chatRoom/delete/{chatRoomId}")
    public String chatRoomDelete(@PathVariable String chatRoomId,
                                 @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                 Model model) {
        if (loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        } else if (loginMember.getRole().equals("admin")) {
            ChatRoom findChatRoom = chatService.findRoomByRoomId(chatRoomId);
            chatService.delete(findChatRoom);
        } else {
            log.info("관리자님이 아닙니다.");

            model.addAttribute("message", "관리자님이 아닙니다.");
            model.addAttribute("redirectUrl", "/");
            return "messages";
        }

        model.addAttribute("message", "채팅방 삭제 성공");
        model.addAttribute("redirectUrl", "/admin/chat");
        return "messages";
    }
}
