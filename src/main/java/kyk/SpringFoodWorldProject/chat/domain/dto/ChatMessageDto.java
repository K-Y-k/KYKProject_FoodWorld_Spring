package kyk.SpringFoodWorldProject.chat.domain.dto;

import kyk.SpringFoodWorldProject.chat.domain.dto.MessageType;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.follow.domain.entity.Follow;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    // 메시지  타입 : 입장, 채팅, 퇴장
    // 입장과 퇴장 ENTER과 LEAVE의 경우 입장/퇴장 이벤트 처리가 실행되고 TALK는 보낸 내용이 해당 채팅방을 구독(sub)하고 있는 모든 클라이언트에게 전달된다.

    private MessageType type; // 메시지 타입
    private String roomId;    // 방 번호
    private String sender;    // 채팅을 보낸 사람 닉네임
    private Long senderId;    // 채팅 보낸 사람 Id
    private String message;   // 메시지
    private String time;      // 채팅 발송 시간

}
