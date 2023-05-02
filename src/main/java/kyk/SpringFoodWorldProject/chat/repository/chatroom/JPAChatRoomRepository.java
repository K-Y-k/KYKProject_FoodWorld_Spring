package kyk.SpringFoodWorldProject.chat.repository.chatroom;


import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JPAChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id);

    @Query("select r from ChatRoom r " +
            "where r.id not in " +
            "(select m.chatRoom.id from ChatMessage m where m.messageType = 'LEAVE' AND m.senderId = :memberId) " +
            "AND (r.member1.id = :memberId OR r.member2.id = :memberId)")
    List<ChatRoom> findNotLeaveMessageRoom(Long memberId);

    ChatRoom findByRoomId(String roomId);

}
