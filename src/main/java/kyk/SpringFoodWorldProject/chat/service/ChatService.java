package kyk.SpringFoodWorldProject.chat.service;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.chat.repository.ChatRoomRepositoryImpl;
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
    private final MemberRepository memberRepository;

    public List<ChatRoom> findMember1ChatRoom(Long member1Id) {
        return chatRoomRepository.findByMember1(member1Id);
    }

    public ChatRoom createChatRoom(Long member1Id, Long member2Id) {
        Member findMember1 = memberRepository.findById(member1Id).orElseThrow(() ->
                new IllegalArgumentException("회원 찾기 실패: " + member1Id));

        Member findMember2 = memberRepository.findById(member2Id).orElseThrow(() ->
                new IllegalArgumentException("회원 찾기 실패: " + member2Id));

        ChatRoom chatRoom = ChatRoom.builder()
                .member1(findMember1)
                .member2(findMember2)
                .build();
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    public ChatRoom findMembersChatRoom(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1_IdAndMember2_Id(member1Id, member2Id);
    }


//    private Map<String, ChatRoomDto> chatRoomMap;
//
//    @PostConstruct
//    private void init() {
//        chatRoomMap = new LinkedHashMap<>();
//    }
//
//    /**
//     * 모든 채팅방 찾기
//     */
//    public List<ChatRoomDto> findAllRoom() {
//        List<ChatRoomDto> chatRooms = new ArrayList<>(chatRoomMap.values());
//        Collections.reverse(chatRooms);
//
//        return chatRooms;
//    }
//
//    /**
//     * 지정한 방 찾기
//     */
//    public ChatRoomDto findRoomById(String roomId) {
//        return chatRoomMap.get(roomId);
//    }
//
//    public ChatRoomDto createRoom(String roomName) {
//        ChatRoomDto chatRoom = new ChatRoomDto().create(roomName); // 채팅룸 이름으로 채팅 룸 생성 후
//        String randomId = UUID.randomUUID().toString();
//
//        // map에 채팅룸 아이디와 만들어진 채팅룸을 저장
//        chatRoomMap.put(randomId, chatRoom);
//        return chatRoom;
//    }

//    public <T> void sendMessage(WebSocketSession session, T message) {
//        try {
//            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }

//    // 채팅방 userName 조회
//    public String getUserName(String roomId, String userUUID){
//        ChatRoomDto room = chatRoomMap.get(roomId);
//        return room.getUserlist().get(userUUID);
//    }
//
//    // 채팅방 유저 리스트 삭제
//    public void delUser(String roomId, String userUUID){
//        ChatRoomDto room = chatRoomMap.get(roomId);
//        room.getUserlist().remove(userUUID);
//    }

}
