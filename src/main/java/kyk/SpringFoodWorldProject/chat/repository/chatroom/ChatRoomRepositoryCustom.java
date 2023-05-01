package kyk.SpringFoodWorldProject.chat.repository.chatroom;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoom> findMemberChatRoom(Long memberId);
    Page<ChatRoom> searchChatRoomByMember(String  memberSearchKeyword, Pageable pageable);
}
