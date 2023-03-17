package kyk.SpringFoodWorldProject.chat.repository.chatmessage;

import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository{

    private final JPAChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public Optional<ChatMessage> findEnterMessage(String roomId, MessageType messageType, Long senderId) {
        return chatMessageRepository.findEnterMessage(roomId, messageType, senderId);
    }

    @Override
    public void deleteByLeaveMessage(String roomId, Long memberId){
        chatMessageRepository.deleteByLeaveMessage(roomId, memberId);
    }


}
