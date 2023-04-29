package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.comment.domain.entity.Comment;
import kyk.SpringFoodWorldProject.comment.service.CommentServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final CommentServiceImpl commentService;

    /**
     * 선택한 게시글의 댓글 Slice 페이징 조회
     */
    @GetMapping("/api/comment")
    public ResponseEntity<?> commentScroll(@RequestParam(value = "boardId") String boardId,
                                           @RequestParam(value = "lastCursorCommentId", defaultValue = "0") Long lastCursorCommentId,
                                           @RequestParam(value = "commentFirst") Boolean first,
                                           @PageableDefault(size=5) Pageable pageable) {
        log.info("lastCursorCommentId={}", lastCursorCommentId);

        return getSlicePaging(boardId, lastCursorCommentId, first, pageable);
    }

    /**
     * 선택한 댓글 삭제 후 Slice 페이징 조회
     */
    @GetMapping("/api/comment/delete")
    public ResponseEntity<?> deleteComment(@RequestParam(value = "boardId") String boardId,
                                           @RequestParam(value = "commentId") String commentId,
                                           @RequestParam(value = "lastCursorCommentId", defaultValue = "0") Long lastCursorCommentId,
                                           @RequestParam(value = "commentFirst") Boolean first,
                                           @PageableDefault(size=5) Pageable pageable) {
        log.info("lastCursorCommentId={}", lastCursorCommentId);

        commentService.delete(Long.valueOf(commentId));

        return getSlicePaging(boardId, lastCursorCommentId, first, pageable);
    }

    private ResponseEntity<?> getSlicePaging(String boardId, Long lastCursorCommentId, Boolean first, Pageable pageable) {
        if (lastCursorCommentId == 0) {
            Long firstCursorBoardId = commentService.findFirstCursorBoardId(Long.valueOf(boardId));
            Slice<Comment> comments = commentService.searchBySlice(firstCursorBoardId, first, pageable, boardId);
            boolean hasNext = comments.hasNext(); // 아래 다시 List -> Slice로 재변환해야할 때 넣기 위해 미리 선언

            // 엔티티의 로직, 매핑된 엔티티의 모두를 가져오는 것 등은 비효율적으로
            // 내가 필요한 필드 형태의 API 스펙을 위해 엔티티 -> DTO로 변환하고
            List<CommentDTO> commentDTOList = comments.stream()
                    .map(m -> new CommentDTO(m.getId(), m.getContent(), m.getCreatedDate(),
                            new MemberDTO(m.getMember().getId(), m.getMember().getName(),
                                    new ProfileFileDTO(m.getMember().getProfileFile().getStoredFileName())),
                            new BoardDTO(m.getBoard().getId(), m.getBoard().getTitle(), m.getBoard().getContent(), m.getBoard().getWriter(), m.getBoard().getBoardType())))
                    .collect(Collectors.toList());

//            return new Result(collect);

            for (CommentDTO comment : commentDTOList) {
                log.info("댓글 json={}", comment.getContent());
            }

            // 위 stream을 사용하기 위해 List로 변환했으므로 다시 Slice 형태로 재변환
            Slice<CommentDTO> sliceComments = new SliceImpl<>(commentDTOList, pageable, hasNext);

            if (sliceComments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(sliceComments, HttpStatus.OK);
            }
        }
        else {
            Slice<Comment> comments = commentService.searchBySlice(lastCursorCommentId, first, pageable, boardId);
            boolean hasNext = comments.hasNext();

            List<CommentDTO> commentDTOList = comments.stream()
                    .map(m -> new CommentDTO(m.getId(), m.getContent(), m.getCreatedDate(),
                            new MemberDTO(m.getMember().getId(), m.getMember().getName(),
                                    new ProfileFileDTO(m.getMember().getProfileFile().getStoredFileName())),
                            new BoardDTO(m.getBoard().getId(), m.getBoard().getTitle(), m.getBoard().getContent(), m.getBoard().getWriter(), m.getBoard().getBoardType())))
                    .collect(Collectors.toList());

            for (CommentDTO comment : commentDTOList) {
                log.info("댓글 json={}", comment.getContent());
            }

            Slice<CommentDTO> sliceComments = new SliceImpl<>(commentDTOList, pageable, hasNext);

            if (sliceComments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(sliceComments, HttpStatus.OK);
            }
        }
    }

    @Data
    @AllArgsConstructor
    static class CommentDTO {
        private Long id;
        private String content;
        private LocalDateTime createdDate;
        private MemberDTO member;
        private BoardDTO board;

    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private Long id;
        private String name;
        private ProfileFileDTO profileFile;
    }

    @Data
    @AllArgsConstructor
    static class ProfileFileDTO {
        private String storedFileName;
    }

    @Data
    @AllArgsConstructor
    static class BoardDTO {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private String boardType;
    }
}
