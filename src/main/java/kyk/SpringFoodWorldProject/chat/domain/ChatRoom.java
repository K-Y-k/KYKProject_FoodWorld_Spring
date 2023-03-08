package kyk.SpringFoodWorldProject.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member1_id")
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "member2_id")
    private Member member2;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMessage> chatMessage = new ArrayList<>();

}
