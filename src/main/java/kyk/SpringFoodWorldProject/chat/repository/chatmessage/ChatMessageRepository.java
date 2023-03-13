package kyk.SpringFoodWorldProject.chat.repository.chatmessage;

import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;

import java.util.List;

public interface ChatMessageRepository {

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessage> findByChatRoom(String roomId);

}
