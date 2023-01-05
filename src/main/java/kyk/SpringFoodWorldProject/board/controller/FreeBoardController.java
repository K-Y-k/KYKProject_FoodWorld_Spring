package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.repository.SpringDataJpaBoardRepository;
import kyk.SpringFoodWorldProject.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class FreeBoardController {

    private final BoardService boardService;

    @GetMapping("/freeBoard")
    public String freeBoards(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
        return "boards/board/freeBoard_main";
    }



}
