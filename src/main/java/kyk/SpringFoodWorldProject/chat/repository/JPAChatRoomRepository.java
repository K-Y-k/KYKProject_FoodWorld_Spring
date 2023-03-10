package kyk.SpringFoodWorldProject.chat.repository;


import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.like.domain.entity.Like;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JPAChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByMember1_Id(Long member1Id);

    ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id);

}
