package kyk.SpringFoodWorldProject.chat.controller;

import kyk.SpringFoodWorldProject.chat.domain.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    /**
     * 메시지 전송
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/chat/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }


    /**
     * 채팅방에 사용자 추가
     */
    @MessageMapping("/chat.addUser")
    @SendTo("/chat/public")
    public ChatMessage addMember(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
