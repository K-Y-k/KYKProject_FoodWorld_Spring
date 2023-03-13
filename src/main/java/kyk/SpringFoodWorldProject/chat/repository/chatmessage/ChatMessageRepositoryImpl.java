package kyk.SpringFoodWorldProject.chat.repository.chatmessage;

import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository{

    private final JPAChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }


    public List<ChatMessage> findByChatRoom(String roomId) {
        return chatMessageRepository.findByChatRoom(roomId);
    }

}
