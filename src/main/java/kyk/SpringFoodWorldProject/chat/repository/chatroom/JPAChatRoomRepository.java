package kyk.SpringFoodWorldProject.chat.repository.chatroom;


import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JPAChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id);

    @Query("select distinct r from ChatRoom r left join r.chatMessages m " +
            "where r not in (select m.chatRoom from ChatMessage m where m.messageType = 'LEAVE' and m.senderId = :memberId)")
    List<ChatRoom> findNotLeaveMessageRoom(Long memberId);

    ChatRoom findByRoomId(String roomId);
}
