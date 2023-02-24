package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateForm;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.domain.entity.BoardFile;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentUploadDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.like.service.LikeServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class MuckstarBoardController {

    private final BoardServiceImpl boardService;
    private final LikeServiceImpl likeService;
    private final CommentServiceImpl commentService;

    @Value("${file.attachFileLocation}")
    private String attachFileLocation;

    /**
     * 글 모두 조회 폼
     */
    @GetMapping("/muckstarBoard")
    public String muckstarBoards(@PageableDefault(page = 0, size = 1, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                 Model model,
                                 BoardSearchCond boardSearchDto) {

        String boardType = "먹스타그램";

        String writerSearchKeyword = boardSearchDto.getWriterSearchKeyword();

        Slice<Board> boards = boardService.searchBySlice(1L, boardSearchDto, pageable, boardType);
        model.addAttribute("boards", boards);



        return "boards/muckstarboard/muckstarBoard_main";
    }

//    @GetMapping("/muckstarBoard")
//    public ResponseEntity<Slice<Board>> muckstarBoards(  @RequestParam(value = "lastCursorId", required = false) Long lastCursorId,
//                                                             @RequestParam(value = "boardSearchDto", required = false) BoardSearchCond boardSearchDto,
//                                                Pageable pageable) {
//        String boardType = "먹스타그램";
//
//        return new ResponseEntity<>(boardService.getSlce(
//                boardService.searchBySlice(lastCursorId, boardSearchDto, pageable, boardType)), HttpStatus.OK);
//
//    }


    /**
     * 글 상세 조회 폼
     */
    @GetMapping("/muckstarBoard/{boardId}")
    public String board_detail(@PathVariable Long boardId,
                        @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                        @ModelAttribute("comment") CommentDto commentDto,
                        Model model) {
        // 조회수 상승
        boardService.updateCount(boardId);

        // 게시글의 번호를 가진 좋아요 row의 개수 가져와서 보냄
        int likeCount = likeService.countByBoard_Id(boardId);
        model.addAttribute("likeCount", likeCount);

        // 좋아요 개수 업데이트
        boardService.updateLikeCount(boardId, likeCount);


        // 댓글 가져오기
        Board board = boardService.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + boardId));
        List<Comment> comments = board.getComments();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        // 파일 가져오기
        List<BoardFile> boardFiles = board.getBoardFiles();
        if (boardFiles != null && !boardFiles.isEmpty()) {
            model.addAttribute("boardFiles", boardFiles);
        }

        model.addAttribute("board", board);

        // 등록한 날이 오늘 날짜이면 시/분까지만 나타나게 조건을 설정하기 위해서 현재 시간을 객체로 담아 보낸 것
        model.addAttribute("localDateTime", LocalDateTime.now());

        return "boards/muckstarboard/muckstarBoard_detail";
    }

    /**
     * 댓글 등록
     */
    @PostMapping("/muckstarBoard/{boardId}/comment")
    public String commentUpload(@PathVariable Long boardId,
                                @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                @ModelAttribute("comment") CommentUploadDto commentDto,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 댓글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        commentService.saveComment(loginMember.getId(), boardId, commentDto);

        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/boards/muckstarBoard/{boardId}";
    }


    /**
     * 글 등록 폼
     */
    @GetMapping("/muckstarBoard/upload")
    public String uploadForm(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             @ModelAttribute("uploadForm") BoardUploadForm boardDto,
                             Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        log.info("로그인 상태 {}", loginMember);
        return "boards/muckstarboard/muckstarBoard_upload";
    }


    /**
     * 글 등록 기능
     */
    @PostMapping("/muckstarBoard/upload")
    public String boardUpload(@SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                              @Valid @ModelAttribute("uploadForm") BoardUploadForm boardDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "boards/muckstarboard/muckstarBoard_upload";
        }

        boardService.upload(loginMember.getId(), boardDto);
        return "redirect:/boards/muckstarBoard";
    }


    /**
     * 글 수정 폼
     */
    @GetMapping("/muckstarBoard/{boardId}/edit")
    public String editForm(@PathVariable Long boardId,
                           Model model) {
        Board board = boardService.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + boardId));

        model.addAttribute("updateForm", board);

        return "boards/muckstarboard/muckstarBoard_edit";
    }

    /**
     * 글 수정 기능
     */
    @PostMapping("/muckstarBoard/{boardId}/edit")
    public String edit(@PathVariable Long boardId,
                       @Valid @ModelAttribute("updateForm") BoardUpdateForm updateParam) {
        boardService.updateBoard(boardId, updateParam);
        return "redirect:/boards/muckstarBoard/{boardId}";
    }


    /**
     * 글 삭제 기능
     */
    @GetMapping("/muckstarBoard/{boardId}/delete")
    public String delete(@PathVariable Long boardId,
                         @SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                         Model model) {
        Board findBoard = boardService.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + boardId));

        if (findBoard.getMember().getId().equals(loginMember.getId())) {
            likeService.deleteByBoard_Id(boardId);
            boardService.delete(boardId);
        } else {
            model.addAttribute("message", "회원님이 작성한 글만 삭제할 수 있습니다!");
            model.addAttribute("redirectUrl", "/boards/muckstarBoard");
            return "messages";
        }

        return "redirect:/boards/muckstarBoard";
    }


    /**
     * 글 좋아요 업데이트 기능
     */
    @GetMapping ("/muckstarBoard/{boardId}/like")
    public String likeUpdate(@PathVariable Long boardId,
                             @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 좋아요를 누를 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }


        likeService.saveLike(loginMember.getId(), boardId);
        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/muckstarBoard/{boardId}";
    }


}