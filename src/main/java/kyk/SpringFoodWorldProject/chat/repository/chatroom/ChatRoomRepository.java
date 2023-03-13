package kyk.SpringFoodWorldProject.chat.repository.chatroom;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);

    List<ChatRoom> findByMember1_IdOrMember2_Id(Long member1Id, Long member2Id);

    ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id);

    ChatRoom findByRoomId(String roomId);
}
