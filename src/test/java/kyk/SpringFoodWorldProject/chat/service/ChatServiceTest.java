package kyk.SpringFoodWorldProject.chat.service;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kyk.SpringFoodWorldProject.chat.domain.dto.MessageType.ENTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ChatServiceTest {

    @Autowired ChatService chatService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        // 회원 데이터 추가
        memberRepository.save(new Member("테스터1", "test", "test!"));
    }

    @Test
    @DisplayName("회원끼리의 채팅방 생성 및 현재 회원들로 구성된 방 찾기")
    void createRoomAndFindRoom() {
        // given
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));

        // when
        ChatRoom createChatRoom = chatService.createChatRoom(savedMember1.getId(), savedMember2.getId());

        // then
        ChatRoom mathcingChatRoom = chatService.findMembersChatRoom(savedMember1.getId(), savedMember2.getId());
        assertThat(createChatRoom.getRoomId()).isEqualTo(mathcingChatRoom.getRoomId());
    }

    @Test
    @DisplayName("현재 회원과 관련된 모든 채팅방들 조회")
    void membersChatRoomList() {
        // given
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));
        Member savedMember3 = memberRepository.save(new Member("ccc", "cc", "cc"));

        ChatRoom createChatRoom1 = chatService.createChatRoom(savedMember1.getId(), savedMember2.getId());
        ChatRoom createChatRoom2 = chatService.createChatRoom(savedMember1.getId(), savedMember3.getId());


        // when
        List<ChatRoom> member1ChatRooms = chatService.findMember1ChatRoom(savedMember1.getId(), savedMember1.getId());


        // then
        assertThat(createChatRoom1.getRoomId()).isEqualTo(member1ChatRooms.get(0).getRoomId());
        assertThat(createChatRoom2.getRoomId()).isEqualTo(member1ChatRooms.get(1).getRoomId());
    }


    @Test
    @DisplayName("채팅 메시지 저장")
    void saveChatMessage() {
        // given
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));

        ChatRoom createChatRoom = chatService.createChatRoom(savedMember1.getId(), savedMember2.getId());

        // when
        ChatMessage chatMessage = chatService.saveChatMessage(new ChatMessageDto(ENTER, createChatRoom.getRoomId(), savedMember1.getName(), savedMember1.getId(), savedMember2.getId(), "메시지", "0000"));

        // then
        assertThat(chatMessage.getContent()).isEqualTo("메시지");
    }

    @Test
    @DisplayName("해당 회원이 입장했던 메시지 저장되어있는지 조회")
    void findEnterMessage() {
        // given
        Member savedMember1 = memberRepository.save(new Member("ddd", "dd", "dd"));
        Member savedMember2 = memberRepository.save(new Member("aaa", "aa", "aa"));

        ChatRoom createChatRoom = chatService.createChatRoom(savedMember1.getId(), savedMember2.getId());

        ChatMessage chatMessage = chatService.saveChatMessage(new ChatMessageDto(ENTER, createChatRoom.getRoomId(), savedMember1.getName(), savedMember1.getId(), savedMember2.getId(), "메시지", "0000"));

        // when
        ChatMessage enterMessage = chatService.findEnterMessage(chatMessage.getChatRoom().getRoomId(), chatMessage.getMessageType(), chatMessage.getSenderId()).orElseThrow(() ->
                new IllegalArgumentException("채팅 메시지 조회 실패"));

        // then
        assertThat(enterMessage.getMessageType()).isEqualTo(ENTER);
    }
}