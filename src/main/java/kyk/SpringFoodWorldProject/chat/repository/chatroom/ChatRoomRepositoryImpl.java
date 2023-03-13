package kyk.SpringFoodWorldProject.chat.repository.chatroom;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final JPAChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public List<ChatRoom> findByMember1_IdOrMember2_Id(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1_IdOrMember2_Id(member1Id, member2Id);
    }

    @Override
    public ChatRoom findByMember1_IdAndMember2_Id(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1_IdAndMember2_Id(member1Id, member2Id);
    }

    @Override
    public ChatRoom findByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }
}
