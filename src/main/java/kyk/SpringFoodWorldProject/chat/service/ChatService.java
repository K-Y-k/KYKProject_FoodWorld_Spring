package kyk.SpringFoodWorldProject.chat.service;

import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.chat.repository.chatmessage.ChatMessageRepositoryImpl;
import kyk.SpringFoodWorldProject.chat.repository.chatroom.ChatRoomRepositoryImpl;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepositoryImpl chatRoomRepository;
    private final ChatMessageRepositoryImpl chatMessageRepository;
    private final MemberRepository memberRepository;


    // 현재 유저와 관련된 모든 채팅방 조회
    public List<ChatRoom> findMember1ChatRoom(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1_IdOrMember2_Id(member1Id, member2Id);
    }

    // 채팅방 생성
    public ChatRoom createChatRoom(Long member1Id, Long member2Id) {
        Member findMember1 = memberRepository.findById(member1Id).orElseThrow(() ->
                new IllegalArgumentException("회원 찾기 실패: " + member1Id));

        Member findMember2 = memberRepository.findById(member2Id).orElseThrow(() ->
                new IllegalArgumentException("회원 찾기 실패: " + member2Id));


        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .member1(findMember1)
                .member2(findMember2)
                .build();

        chatRoomRepository.save(chatRoom);

        return chatRoom;
    }

    // 현재 유저와 관련된 채팅방 조회
    public ChatRoom findMembersChatRoom(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1_IdAndMember2_Id(member1Id, member2Id);
    }

    // 채팅방 번호로 채팅방 조회
    public ChatRoom findRoomByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }




    public ChatMessage saveChatMessage(ChatMessageDto chatMessageDto) {

        ChatRoom findChatRoom = chatRoomRepository.findByRoomId(chatMessageDto.getRoomId());

        ChatMessage chatMessageEntity = ChatMessage.builder()
                .chatRoom(findChatRoom)
                .messageType(chatMessageDto.getType())
                .content(chatMessageDto.getMessage())
                .senderId(chatMessageDto.getSenderId())
                .senderName(chatMessageDto.getSender())
                .build();

        return chatMessageRepository.save(chatMessageEntity);
    }

}
