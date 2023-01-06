package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class FreeBoardController {

    private final BoardService boardService;

    /**
     * 글 모두 조회 폼
     */
    @GetMapping("/freeBoard")
    public String freeBoards(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
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
    public String uploadForm(@ModelAttribute("board") Board board) {
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
    public String edit(@PathVariable Long boardId, @ModelAttribute BoardUpdateDto updateParam) {
        boardService.update(boardId, updateParam);
        return "redirect:/boards/freeBoard/{boardId}";
    }





}
