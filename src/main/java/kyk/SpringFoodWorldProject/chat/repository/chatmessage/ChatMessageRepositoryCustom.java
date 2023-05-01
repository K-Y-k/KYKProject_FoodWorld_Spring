package kyk.SpringFoodWorldProject.chat.repository.chatmessage;

import kyk.SpringFoodWorldProject.admin.dto.AdminChatMessageDTO;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    Long findFirstCursorChatMessageId(String chatRoomId);
    Slice<AdminChatMessageDTO> searchBySlice(Long lastCursorChatMessageId, Boolean first, Pageable pageable, String chatRoomId);
}
