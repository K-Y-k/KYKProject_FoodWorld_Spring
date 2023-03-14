package kyk.SpringFoodWorldProject.chat.repository.chatroom;


import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JPAChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByMember1_IdOrMember2_Id(Long member1Id, Long member2Id);

    ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id);

//    @Query("select r from ChatRoom r left join r.ChatMessages m " +
//            "where r.id = :roomId and m.id not in (select m2.id from ChatMessage m2 where m2.messageType = 'LEAVE' and m2.senderId = :memberId)")
//    ChatRoom findRoom(String roomId, Long memberId);

    ChatRoom findByRoomId(String roomId);
}
