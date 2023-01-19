package kyk.SpringFoodWorldProject.board.controller;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardDto;
import kyk.SpringFoodWorldProject.board.domain.dto.BoardUpdateDto;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentDto;
import kyk.SpringFoodWorldProject.comment.domain.dto.CommentResponseDto;
import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import kyk.SpringFoodWorldProject.like.service.LikeServiceImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class FreeBoardController {

    private final BoardServiceImpl boardService;
    private final LikeServiceImpl likeService;

    private final CommentServiceImpl commentService;


    /**
     * 글 모두 조회 폼
     */
    @GetMapping("/freeBoard")
    public String freeBoards(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                             Model model,
                             String writerSearchKeyword,
                             String titleSearchKeyword) {
        Page<Board> boards = null;

        // 키워드의 컬럼에 따른 페이징된 게시글 출력
        if (writerSearchKeyword == null && titleSearchKeyword == null) {           // 검색 자체를 하지 않았을 때
            boards = boardService.pageList(pageable);
        } else if (writerSearchKeyword.equals("") && titleSearchKeyword != null) { // 제목만 검색했을 때
            boards = boardService.findByTitleContaining(titleSearchKeyword, pageable);
        } else if (writerSearchKeyword != null && titleSearchKeyword.equals("")) { // 글쓴이만 검색했을 때
            boards = boardService.findByWriterContaining(writerSearchKeyword, pageable);
        } else {
            boards = boardService.findByTitleContainingAndWriterContaining(titleSearchKeyword, writerSearchKeyword, pageable);
        }

        int nowPage = pageable.getPageNumber() + 1; // 페이지에 넘어온 페이지를 가져옴 == boards.getPageable().getPageNumber()
                                                    // pageable의 index는 0부터 시작이기에 1을 더해준 것
        int startPage = Math.max(1, nowPage - 2);   // 마이너스가 나오지 않게 max로 최대 1로 조정
        int endPage = Math.min(nowPage + 2, boards.getTotalPages()); // 토탈 페이지보다 넘지 않게 min으로 조정

        model.addAttribute("boards", boards);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 현재 시간 비교를 위해
        model.addAttribute("localDateTime", LocalDateTime.now());

        // 이전, 다음으로 적용
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        // 이전, 다음 페이지의 존재 여부
        model.addAttribute("hasPrev", boards.hasPrevious());
        model.addAttribute("hasNext", boards.hasNext());

        return "boards/board/freeBoard_main";
    }


    /**
     * 글 상세 조회 폼
     */
    @GetMapping("/freeBoard/{boardId}")
    public String board(@PathVariable Long boardId,
                        @SessionAttribute(name="loginMember", required = false) Member loginMember,
                        @ModelAttribute("comment") CommentDto commentDto,
                        Model model) {
        boardService.updateCount(boardId); // 조회수 상승

        Board board = boardService.findById(boardId).orElseThrow();

        // 게시글의 번호를 가진 좋아요 row의 개수 가져와서 보냄
        int likeCount = likeService.countByBoard_Id(boardId);
        model.addAttribute("likeCount", likeCount);

        // 좋아요 개수 업데이트
        boardService.updateLikeCount(boardId, likeCount);


        // 댓글 가져오기
        List<Comment> comments = board.getComment();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("board", board);

        // 등록한 날이 오늘 날짜이면 시/분까지만 나타나게 조건을 설정하기위해서 현재 시간을 객체로 담아 보낸 것
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "boards/board/freeBoard_detail";
    }

    /**
     * 댓글 등록
     */
    @PostMapping("/freeBoard/{boardId}/comment")
    public String commentUpload(@PathVariable Long boardId,
                                @SessionAttribute(name="loginMember", required = false) Member loginMember,
                                @ModelAttribute("comment") CommentDto commentDto,
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
        log.info("댓글 저장 성공");

        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/boards/freeBoard/{boardId}";
    }


    /**
     * 글 등록 폼
     */
    @GetMapping("/freeBoard/upload")
    public String uploadForm(@SessionAttribute(name="loginMember", required = false) Member loginMember,
                             @ModelAttribute("board") BoardDto boardDto,
                             Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }

        log.info("로그인 상태 {}", loginMember);
        return "boards/board/freeBoard_upload";
    }


    /**
     * 글 등록 기능
     */
    @PostMapping("/freeBoard/upload")
    public String boardUpload(@ModelAttribute("board") BoardDto boardDto,
                              @SessionAttribute("loginMember") Member member) throws Exception {
        log.info("boardDto={}", boardDto);
        log.info("loginMember={}", member);
        boardService.upload(member.getId(), boardDto);

//        return ResponseEntity.ok(boardService.save(member.getName(), boardDto));
//
//        UploadFile attachFile = boardService.storeFile(board.getAttachFile());
//        List<UploadFile> storeImageFiles = boardService.storeFiles(board.getImageFiles());
//
//        Board boardDto = new Board();
//        boardDto.setTitle(board.getTitle());
//        boardDto.setContent(board.getContent());
//        boardDto.setAttachFile((MultipartFile) attachFile);
//
//        boardService.save(boardDto);

        return "redirect:/boards/freeBoard";
    }


    /**
     * 글 수정 폼
     */
    @GetMapping("/freeBoard/{boardId}/edit")
    public String editForm(@PathVariable Long boardId,
                           Model model) {
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
        boardService.updateBoard(boardId, updateParam);
        return "redirect:/boards/freeBoard/{boardId}";
    }


    /**
     * 글 삭제 기능
     */
    @GetMapping("/freeBoard/{boardId}/delete")
    public String delete(@PathVariable Long boardId,
                         @SessionAttribute("loginMember") Member loginMember,
                         Model model) {
        Board findBoard = boardService.findById(boardId).orElseThrow();

        if (findBoard.getMember().getId().equals(loginMember.getId())) {
            boardService.delete(boardId);
        } else {
            model.addAttribute("message", "회원님이 작성한 글만 삭제할 수 있습니다!");
            model.addAttribute("redirectUrl", "/boards/freeBoard");
            return "messages";
        }

        return "redirect:/boards/freeBoard";
    }


//    /**
//     * 글 좋아요 체크 기능
//     */
//    @GetMapping("/freeBoard/{boardId}/like")
//    public String like(@PathVariable Long boardId,
//                       @SessionAttribute("loginMember") Member loginMember,
//                       RedirectAttributes redirectAttributes,
//                       Model model) {
//        Board findBoard = boardService.findById(boardId).orElseThrow();
//
//        likeService.findLike(loginMember.getId(), boardId);
//        redirectAttributes.addAttribute("boardId", boardId);
//
//        return "redirect:/boards/freeBoard/{boardId}";
//    }


    /**
     * 글 좋아요 업데이트
     */
    @GetMapping ("/freeBoard/{boardId}/like")
    public String likeUpdate(@PathVariable Long boardId,
                             @SessionAttribute(value = "loginMember", required = false) Member loginMember,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        // 세션에 회원 데이터가 없으면 홈 화면으로 이동
        if(loginMember == null) {
            log.info("로그인 상태가 아님");

            model.addAttribute("message", "회원만 글을 작성할 수 있습니다. 로그인 먼저 해주세요!");
            model.addAttribute("redirectUrl", "/members/login");
            return "messages";
        }


        likeService.saveLike(loginMember.getId(), boardId);
        redirectAttributes.addAttribute("boardId", boardId);

        return "redirect:/boards/freeBoard/{boardId}";
    }




}
