package kyk.SpringFoodWorldProject.chat.controller;

import kyk.SpringFoodWorldProject.chat.domain.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
import kyk.SpringFoodWorldProject.chat.service.ChatService;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    // convertAndSend를 사용하기 위해서 선언
    // convertAndSend는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
    private final SimpMessageSendingOperations template;

    private final ChatService chatService;


    // MessageMapping을 통해 웹 소켓으로 들어오는 메시지를 발신 처리한다.
    // 이때 클라이언트에서는 /pub/chat/message로 요청하게 되고 이것을 controller가 받아서 처리한다.
    // 처리가 완료되면 /sub/chat/room/roomId로 메시지가 전송된다.
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatMessageDto messageDto, SimpMessageHeaderAccessor headerAccessor) {

        messageDto.setMessage(messageDto.getSender() + " 입장!!");
        template.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
        chatService.saveChatMessage(messageDto);
    }

    // 해당 유저 메세지 전송
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatMessageDto messageDto) {
        log.info("CHAT {}", messageDto);


        messageDto.setMessage(messageDto.getMessage());
        template.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), messageDto);
        chatService.saveChatMessage(messageDto);

    }


    // 회원 퇴장 시 퇴장 메시지 저장 및 EventListener을 통해서 퇴장 알림
//    @EventListener
//    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        log.info("DisConnEvent {}", event);
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//
//        // 채팅방 유저 리스트에서 퇴장한 회원 조회 및 삭제
//
//
//        // 퇴장 알림
//        ChatMessageDto chat = ChatMessageDto.builder()
//                .type(MessageType.LEAVE)
//                .sender(username)
//                .message(username + " 퇴장!!")
//                .build();
//
//        template.convertAndSend("/sub/chat/room/" + roomId, chat);
//    }


}
