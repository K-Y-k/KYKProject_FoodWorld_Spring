package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateForm;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
public class RecommendBoardController {

    private final BoardServiceImpl boardService;
    private final LikeServiceImpl likeService;
    private final CommentServiceImpl commentService;

    @Value("${file.attachFileLocation}")
    private String attachFileLocation;

    /**
     * 글 모두 조회 폼
     */
    @GetMapping("/recommendBoard")
    public String recommendBoards(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model,
                             BoardSearchCond boardSearchDto) {
        Page<Board> boards;
        String boardType = "추천게시판";

        String writerSearchKeyword = boardSearchDto.getWriterSearchKeyword();
        String titleSearchKeyword = boardSearchDto.getTitleSearchKeyword();

        // 키워드의 컬럼에 따른 페이징된 게시글 출력
        if (writerSearchKeyword == null && titleSearchKeyword == null) {  // 검색 자체를 하지 않았을 때 : 검색을 클릭할 때 url로 파라미터가 표현되므로 검색 클릭 안했을 때는 null
            boards = boardService.findPageListByBoardType(pageable, boardType);
        } else if (writerSearchKeyword.isEmpty()) {                      // 제목만 검색했을 때 : url로 표현된 파라미터가 비어있을 때는 빈공백이므로 .isEmpty()로 표현
            boards = boardService.findByTitleContaining(titleSearchKeyword, pageable, boardType);
        } else if (titleSearchKeyword.isEmpty()) {                       // 글쓴이만 검색했을 때
            boards = boardService.findByWriterContaining(writerSearchKeyword, pageable, boardType);
        } else {
            boards = boardService.findByTitleContainingAndWriterContaining(titleSearchKeyword, writerSearchKeyword, pageable, boardType);
        }

        // 댓글 개수 가져오는 작업
        for (Board board : boards) {
            Board findBoard = boardService.findById(board.getId()).orElseThrow(() ->
                    new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + board.getId()));
            boardService.updateCommentCount(findBoard.getId());
        }

        int nowPage = pageable.getPageNumber() + 1;                  // 페이지에 넘어온 페이지를 가져옴 == boards.getPageable().getPageNumber()
        // pageable의 index는 0부터 시작이기에 1을 더해준 것이다.

        int startPage = Math.max(1, nowPage - 2);                    // 마이너스가  나오지 않게 Math.max로 최대 1로 조정
        int endPage = Math.min(nowPage + 2, boards.getTotalPages()); // 총 페이지보다 넘지 않게 Math.min으로 조정


        model.addAttribute("boards", boards);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 등록한 날이 오늘 날짜이면 시/분까지만 나타나게 조건을 설정하기 위해서 현재 시간을 객체로 담아 보낸 것
        model.addAttribute("localDateTime", LocalDateTime.now());

        // 이전, 다음으로 적용
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        // 이전, 다음 페이지의 존재 여부
        model.addAttribute("hasPrev", boards.hasPrevious());
        model.addAttribute("hasNext", boards.hasNext());

        return "boards/recommendboard/recommendBoard_main";
    }


    /**
     * 글 상세 조회 폼
     */
    @GetMapping("/recommendBoard/{boardId}")
    public String board(@PathVariable Long boardId,
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

        return "boards/recommendboard/recommendBoard_detail";
    }

    /**
     * 댓글 등록
     */
    @PostMapping("/recommendBoard/{boardId}/comment")
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
        return "redirect:/boards/recommendBoard/{boardId}";
    }


    /**
     * 글 등록 폼
     */
    @GetMapping("/recommendBoard/upload")
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
        return "boards/recommendboard/recommendBoard_upload";
    }


    /**
     * 글 등록 기능
     */
    @PostMapping("/recommendBoard/upload")
    public String boardUpload(@SessionAttribute(LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                              @Valid @ModelAttribute("uploadForm") BoardUploadForm boardDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "boards/recommendboard/recommendBoard_upload";
        }

        boardService.upload(loginMember.getId(), boardDto);
        return "redirect:/boards/recommendBoard";
    }


    /**
     * 글 수정 폼
     */
    @GetMapping("/recommendBoard/{boardId}/edit")
    public String editForm(@PathVariable Long boardId,
                           Model model) {
        Board board = boardService.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + boardId));

        model.addAttribute("updateForm", board);

        return "boards/recommendboard/recommendBoard_edit";
    }

    /**
     * 글 수정 기능
     */
    @PostMapping("/recommendBoard/{boardId}/edit")
    public String edit(@PathVariable Long boardId,
                       @ModelAttribute("updateForm") BoardUpdateForm updateParam) {
        boardService.updateBoard(boardId, updateParam);
        return "redirect:/boards/recommendBoard/{boardId}";
    }


    /**
     * 글 삭제 기능
     */
    @GetMapping("/recommendBoard/{boardId}/delete")
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
            model.addAttribute("redirectUrl", "/boards/recommendBoard");
            return "messages";
        }

        return "redirect:/boards/recommendBoard";
    }


    /**
     * 글 좋아요 업데이트 기능
     */
    @GetMapping ("/recommendBoard/{boardId}/like")
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

        return "redirect:/boards/recommendBoard/{boardId}";
    }


}