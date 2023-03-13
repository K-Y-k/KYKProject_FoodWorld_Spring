package kyk.SpringFoodWorldProject.chat.repository.chatmessage;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JPAChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom(String roomId);

}
