package kyk.SpringFoodWorldProject.chat.repository.chatroom;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<ChatRoom> findMemberChatRoom(Long memberId);
}
