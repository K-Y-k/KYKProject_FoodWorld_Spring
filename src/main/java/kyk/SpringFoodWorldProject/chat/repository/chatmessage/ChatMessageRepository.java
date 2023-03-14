package kyk.SpringFoodWorldProject.chat.repository.chatmessage;


import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {

    /**
     * 메시지 저장
     */
    ChatMessage save(ChatMessage chatMessage);


    /**
     * 해당 방의 메시지 모두 조회
     */
    List<ChatMessage> findByChatRoom(String roomId);


    /**
     * 해당 방에 현재 회원이 이전에 채팅 입장 메시지가 저장되어있는지 조회
     */
    Optional<ChatMessage> findEnterMessage(String roomId, MessageType messageType, Long senderId);
}
