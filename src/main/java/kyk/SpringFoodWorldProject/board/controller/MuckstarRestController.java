//package kyk.SpringFoodWorldProject.board.controller;
//
//import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
//import kyk.SpringFoodWorldProject.board.domain.entity.Board;
//import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/boards")
//public class MuckstarRestController {
//
//    private final BoardServiceImpl boardService;
//
//    /**
//     * 글 모두 조회 폼
//     */
//    @GetMapping("/muckstarBoard")
//    public String muckstarBoards(@PageableDefault(page = 0, size = 1, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                                 Model model,
//                                 BoardSearchCond boardSearchDto) {
//
//        String boardType = "먹스타그램";
//
//        String writerSearchKeyword = boardSearchDto.getWriterSearchKeyword();
//
//        Slice<Board> boards = boardService.searchBySlice(1L, boardSearchDto, pageable, boardType);
//        model.addAttribute("boards", boards);
//
//
//
//        return "boards/muckstarboard/muckstarBoard_main";
//    }
//}
