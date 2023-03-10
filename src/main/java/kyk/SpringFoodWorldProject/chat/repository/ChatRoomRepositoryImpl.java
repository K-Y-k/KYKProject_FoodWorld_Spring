package kyk.SpringFoodWorldProject.chat.repository;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final JPAChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoom> findByMember1(Long member1Id) {
        return chatRoomRepository.findByMember1_Id(member1Id);
    }

    @Override
    public ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1_IdAndMember2_Id(member1Id, member2Id);
    }
}
