package kyk.SpringFoodWorldProject.chat.domain.entity;

import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.chat.domain.MessageType;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatMessage_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    private Long senderId;

    private String senderName;

    private String content;


}
