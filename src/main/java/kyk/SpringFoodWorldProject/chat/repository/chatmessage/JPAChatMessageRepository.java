package kyk.SpringFoodWorldProject.chat.repository.chatmessage;

import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JPAChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom(String roomId);

    @Query("select m from ChatMessage m left join m.chatRoom r " +
            "where r.id = :roomId and m.messageType = :messageType and m.senderId = :senderId")
    Optional<ChatMessage> findEnterMessage(String roomId, MessageType messageType, Long senderId);

    @Modifying
    @Query("delete from ChatMessage m where m.chatRoom.id = :roomId and m.messageType = 'LEAVE' and m.senderId = :memberId")
    void deleteByLeaveMessage(String roomId, Long memberId);

}
