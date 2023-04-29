package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.service.ChatService;
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
    private final ChatService chatService;

    /**
     * 선택한 게시글의 댓글 Slice 페이징 조회
     */
    @GetMapping("/api/comment")
    public ResponseEntity<?> commentScroll(@RequestParam(value = "boardId") String boardId,
                                           @RequestParam(value = "lastCursorCommentId", defaultValue = "0") Long lastCursorCommentId,
                                           @RequestParam(value = "commentFirst") Boolean first,
                                           @PageableDefault(size=5) Pageable pageable) {
        log.info("lastCursorCommentId={}", lastCursorCommentId);

        return getCommentSlicePaging(boardId, lastCursorCommentId, first, pageable);
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

        return getCommentSlicePaging(boardId, lastCursorCommentId, first, pageable);
    }

    private ResponseEntity<?> getCommentSlicePaging(String boardId, Long lastCursorCommentId, Boolean first, Pageable pageable) {
        if (lastCursorCommentId == 0) {
            Long firstCursorCommentId = commentService.findFirstCursorCommentId(Long.valueOf(boardId));
            Slice<Comment> comments = commentService.searchBySlice(firstCursorCommentId, first, pageable, boardId);
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

    /**
     * 선택한 채팅방의 채팅 메시지 Slice 페이징 조회
     */
    @GetMapping("/api/chatMessage")
    public ResponseEntity<?> chatMessageScroll(@RequestParam(value = "chatRoomId") String chatRoomId,
                                               @RequestParam(value = "lastCursorChatMessageId", defaultValue = "0") Long lastCursorChatMessageId,
                                               @RequestParam(value = "chatMessageFirst") Boolean first,
                                               @PageableDefault(size=5) Pageable pageable) {
        log.info("lastCursorChatMessageId={}", lastCursorChatMessageId);

        return getChatMessageSlicePaging(chatRoomId, lastCursorChatMessageId, first, pageable);
    }

    /**
     * 선택한 채팅 메시지 삭제 후 Slice 페이징 조회
     */
    @GetMapping("/api/chatMessage/delete")
    public ResponseEntity<?> deleteChatMessage(@RequestParam(value = "chatRoomId") String chatRoomId,
                                               @RequestParam(value = "chatMessageId") String chatMessageId,
                                               @RequestParam(value = "lastCursorChatMessageId", defaultValue = "0") Long lastCursorChatMessageId,
                                               @RequestParam(value = "chatMessageFirst") Boolean first,
                                               @PageableDefault(size=5) Pageable pageable) {
        log.info("lastCursorChatMessageId={}", lastCursorChatMessageId);
        log.info("chatMessageId={}", chatMessageId);
        log.info("chatRoomId={}", chatRoomId);

        chatService.deleteChatMessage(Long.valueOf(chatMessageId));

        return getChatMessageSlicePaging(chatRoomId, lastCursorChatMessageId, first, pageable);
    }

    private ResponseEntity<?> getChatMessageSlicePaging(String chatRoomId, Long lastCursorChatMessageId, Boolean first, Pageable pageable) {
        if (lastCursorChatMessageId == 0) {
            Long firstCursorChatMessageId = chatService.findFirstCursorChatMessageId(chatRoomId);
            Slice<ChatMessage> chatMessages = chatService.searchBySlice(firstCursorChatMessageId, first, pageable, chatRoomId);
            boolean hasNext = chatMessages.hasNext();

            List<ChatMessageDTO> chatMessageDTOList = chatMessages.stream()
                    .map(m -> new ChatMessageDTO(m.getId(), m.getCreatedDate(), m.getContent(), m.getSender(), m.getSenderProfile(),
                                                 new ChatRoomDTO(m.getChatRoom().getRoomId(),
                                                 new MemberDTO(m.getChatRoom().getMember1().getId(), m.getChatRoom().getMember1().getName(),
                                                               new ProfileFileDTO(m.getChatRoom().getMember1().getProfileFile().getStoredFileName())),
                                                 new MemberDTO(m.getChatRoom().getMember2().getId(), m.getChatRoom().getMember2().getName(),
                                                               new ProfileFileDTO(m.getChatRoom().getMember2().getProfileFile().getStoredFileName())))))
                    .collect(Collectors.toList());

            for (ChatMessageDTO chatMessage : chatMessageDTOList) {
                log.info("채팅 메시지 json={}", chatMessage.getContent());
            }

            Slice<ChatMessageDTO> sliceComments = new SliceImpl<>(chatMessageDTOList, pageable, hasNext);

            if (sliceComments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(sliceComments, HttpStatus.OK);
            }
        }
        else {
            Slice<ChatMessage> chatMessages = chatService.searchBySlice(lastCursorChatMessageId, first, pageable, chatRoomId);
            boolean hasNext = chatMessages.hasNext();

            List<ChatMessageDTO> chatMessageDTOList = chatMessages.stream()
                    .map(m -> new ChatMessageDTO(m.getId(), m.getCreatedDate(), m.getContent(), m.getSender(), m.getSenderProfile(),
                              new ChatRoomDTO(m.getChatRoom().getRoomId(),
                                              new MemberDTO(m.getChatRoom().getMember1().getId(), m.getChatRoom().getMember1().getName(),
                                                            new ProfileFileDTO(m.getChatRoom().getMember1().getProfileFile().getStoredFileName())),
                                              new MemberDTO(m.getChatRoom().getMember2().getId(), m.getChatRoom().getMember2().getName(),
                                                            new ProfileFileDTO(m.getChatRoom().getMember2().getProfileFile().getStoredFileName())))))
                    .collect(Collectors.toList());

            for (ChatMessageDTO chatMessage : chatMessageDTOList) {
                log.info("채팅 메시지 json={}", chatMessage.getContent());
            }

            Slice<ChatMessageDTO> sliceComments = new SliceImpl<>(chatMessageDTOList, pageable, hasNext);

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

    @Data
    @AllArgsConstructor
    static class ChatMessageDTO {
        private Long id;
        private LocalDateTime createdDate;
        private String content;
        private String sender;
        private String senderProfile;
        private ChatRoomDTO chatRoom;
    }

    @Data
    @AllArgsConstructor
    static class ChatRoomDTO {
        private String roomId;
        private MemberDTO member1;
        private MemberDTO member2;
    }
}
