package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.board.service.BoardService;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class FreeBoardController {

    private final BoardServiceImpl boardService;

//    @RequestParam(value = "titleSearchKeyword", required=false) String titleSearchKeyword,
//    @RequestParam(value = "writerSearchKeyword", required=false) String writerSearchKeyword

    /**
     * 글 모두 조회 폼
     */
    @GetMapping("/freeBoard")
    public String freeBoards(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model,
                             @ModelAttribute("boardDto") BoardDto boardDto) {
        Page<Board> boards = null;

        if (boardDto.getTitleSearchKeyword() == null && boardDto.getWriterSearchKeyword() == null) {
            boards = boardService.pageList(pageable);
        } else if (boardDto.getWriterSearchKeyword() == null && boardDto.getTitleSearchKeyword() != null ) {
            boards = boardService.findByTitleContaining(boardDto.getTitleSearchKeyword(), pageable);
        } else if (boardDto.getWriterSearchKeyword() != null && boardDto.getTitleSearchKeyword() == null) {
            boards = boardService.findByWriterContaining(boardDto.getWriterSearchKeyword(), pageable);
        }


        int nowPage = pageable.getPageNumber() + 1; // 페이지에 넘어온 페이지를 가져옴 == boards.getPageable().getPageNumber()
                                                    // pageable은 0부터 시작이기에 1을 더해준 것
        int startPage = Math.max(nowPage - 4, 1);   // 마이너스가 나오지 않게 max로 최대 1로 조정
        int endPage = Math.min(nowPage + 5, boards.getTotalPages()); // 토탈 페이지보다 넘지 않게 min으로 조정

        model.addAttribute("boards", boards);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("hasNext", boards.hasNext());
        model.addAttribute("hasPrev", boards.hasPrevious());

        return "boards/board/freeBoard_main";
    }

    /**
     * 글 상세 조회 폼
     */
    @GetMapping("/freeBoard/{boardId}")
    public String board(@PathVariable long boardId, Model model) {
        Board board = boardService.findById(boardId).get();
        model.addAttribute("board", board);
        return "boards/board/freeBoard_detail";
    }


    /**
     * 글 등록 폼
     */
    @GetMapping("/freeBoard/upload")
    public String uploadForm(@SessionAttribute(name="loginMember", required = false) Member loginMember,
                             Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");
            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        log.info("로그인 성공한 상태 {}", loginMember);

        model.addAttribute("board", new Board());
        return "boards/board/freeBoard_upload";
    }

    /**
     * 글 등록 기능
     */
    @PostMapping("/freeBoard/upload")
    public String upload(@ModelAttribute("board") Board board, RedirectAttributes redirectAttributes) {
        Board savedBoard = boardService.save(board);
        redirectAttributes.addAttribute("boardId", savedBoard.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/boards/freeBoard";
    }

    /**
     * 글 수정 폼
     */
    @GetMapping("/freeBoard/{boardId}/edit")
    public String editForm(@PathVariable Long boardId, Model model) {
        Board board = boardService.findById(boardId).get();
        model.addAttribute("board", board);
        return "boards/board/freeBoard_edit";
    }

    /**
     * 글 수정 기능
     */
    @PostMapping("/freeBoard/{boardId}/edit")
    public String edit(@PathVariable Long boardId,
                       @ModelAttribute("board") BoardUpdateDto updateParam) {
        boardService.update(boardId, updateParam);
        return "redirect:/boards/freeBoard/{boardId}";
    }


    /**
     * 글 삭제 기능
     */
    @GetMapping("/freeBoard/{boardId}/delete")
    public String delete(@PathVariable Long boardId) {
        boardService.delete(boardId);
        return "redirect:/boards/freeBoard";
    }





}
