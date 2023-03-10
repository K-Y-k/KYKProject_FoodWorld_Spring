//package kyk.SpringFoodWorldProject.chat.controller;
//
//import kyk.SpringFoodWorldProject.chat.domain.MessageType;
//import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDTO;
//import kyk.SpringFoodWorldProject.chat.service.ChatService;
//import kyk.SpringFoodWorldProject.member.domain.entity.Member;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class ChatController {
//
//    // convertAndSend를 사용하기 위해서 선언
//    // convertAndSend는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
//    private final SimpMessageSendingOperations template;
//
//    @Autowired ChatService chatService;
//
//    // MessageMapping을 통해 webSocket로 들어오는 메시지를 발신 처리한다.
//    // 이때 클라이언트에서는 /pub/chat/message로 요청하게 되고 이것을 controller가 받아서 처리한다.
//    // 처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.
//    @MessageMapping("/chat/enterUser")
//    public void enterUser(@Payload ChatMessageDTO chat, SimpMessageHeaderAccessor headerAccessor) {
//
//        // 반환 결과를 socket session에 userUUID 로 저장
//        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
//
//        chat.setMessage(chat.getSender() + " 님 입장!!");
//        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
//
//    }
//
//    // 해당 유저
//    @MessageMapping("/chat/sendMessage")
//    public void sendMessage(@Payload ChatMessageDTO chat) {
//        log.info("CHAT {}", chat);
//        chat.setMessage(chat.getMessage());
//        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);
//
//    }
//
//    // 유저 퇴장 시에는 EventListener을 통해서 유저 퇴장을 확인
//    @EventListener
//    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        log.info("DisConnEvent {}", event);
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
//        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//
//        log.info("headAccessor {}", headerAccessor);
//
//        // 채팅방 유저 -1
//
//        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
//        String username = chatService.getUserName(roomId, userUUID);
//        chatService.delUser(roomId, userUUID);
//
//        if (username != null) {
//            log.info("User Disconnected : " + username);
//
//            // builder 어노테이션 활용
//            ChatMessageDTO chat = ChatMessageDTO.builder()
//                    .type(MessageType.LEAVE)
//                    .sender(username)
//                    .message(username + " 님 퇴장!!")
//                    .build();
//
//            template.convertAndSend("/sub/chat/room/" + roomId, chat);
//        }
//    }
//
//
//
////    /**
////     * 메시지 전송
////     */
////    @MessageMapping("/chat.sendMessage")
////    @SendTo("/chat/public")
////    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
////        return chatMessage;
////    }
////
////
////    /**
////     * 채팅방에 사용자 추가
////     */
////    @MessageMapping("/chat.addUser")
////    @SendTo("/chat/public")
////    public ChatMessage addMember(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
////        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
////        return chatMessage;
////    }
//}
