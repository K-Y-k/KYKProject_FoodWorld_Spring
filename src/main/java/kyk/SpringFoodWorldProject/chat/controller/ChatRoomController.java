package kyk.SpringFoodWorldProject.chat.controller;

import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.chat.service.ChatService;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService chatService;

    
    /**
     * 채팅방 기본 폼
     */
    @GetMapping("")
    public String chatRoomList(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             Model model){

        if (loginMember != null) {
            List<ChatRoom> member1ChatRoom = chatService.findMember1ChatRoom(loginMember.getId(), loginMember.getId());

            if (!member1ChatRoom.isEmpty()) {
                model.addAttribute("member1ChatRoom", member1ChatRoom);
            }
        }

        return "chat/chat";
    }


    /**
     * 채팅하기 버튼 클릭시 채팅방 매칭
     */
    @GetMapping("/matchingRoom/{memberId}")
    public String goChatRoom(@PathVariable Long memberId,
                             @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                             RedirectAttributes redirectAttributes,
                             Model model){

        // 현재 회원과 채팅을 원하는 상대 회원의 채팅방을 찾고
        ChatRoom findMembersChatRoom1 = chatService.findMembersChatRoom(loginMember.getId(), memberId);
        ChatRoom findMembersChatRoom2 = chatService.findMembersChatRoom(memberId, loginMember.getId());

        if (findMembersChatRoom1 == null && findMembersChatRoom2 == null) { // 채팅방이 없으면 새로 만든 후 전체 채팅방 조회
            chatService.createChatRoom(loginMember.getId(), memberId);

            List<ChatRoom> member1ChatRoom = chatService.findMember1ChatRoom(loginMember.getId(), loginMember.getId());
            redirectAttributes.addFlashAttribute("member1ChatRoom", member1ChatRoom);
        } else { // 기존에 있으면 전체 채팅방 조회
            List<ChatRoom> member1ChatRoom = chatService.findMember1ChatRoom(loginMember.getId(), loginMember.getId());
            redirectAttributes.addFlashAttribute("member1ChatRoom", member1ChatRoom);
        }
        return "redirect:/chat";
    }


    /**
     * 클릭한 채팅방 조회
     */
    @GetMapping("/room")
    public String goChatRoom(@RequestParam String roomId,
                             @SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER) Member loginMember,
                             Model model){
        // 클릭한 방을 조회
        ChatRoom targetChatRoom = chatService.findRoomByRoomId(roomId);
        model.addAttribute("targetChatRoom", targetChatRoom);

        // 클릭한 방의 메시지 가져오기
        List<ChatMessage> chatMessage = targetChatRoom.getChatMessage();
        model.addAttribute("chatMessage", chatMessage);

        // 현재 회원의 전체 채팅방 리스트
        List<ChatRoom> member1ChatRoom = chatService.findMember1ChatRoom(loginMember.getId(), loginMember.getId());
        model.addAttribute("member1ChatRoom", member1ChatRoom);

        return "chat/chat";
    }


//
//
//    // 채팅방 생성 : 채팅방 생성 후 다시 return
//    @PostMapping("/chat/createroom/{memberId}")
//    public String createRoom(@RequestParam String name, RedirectAttributes redirectAttributes) {
//        ChatRoomDto room = chatService.createRoom(name);
//        log.info("CREATE Chat Room {}", room);
//
//        redirectAttributes.addFlashAttribute("roomName", room);
//        return "redirect:/chat";
//    }
//
//    // 채팅방 입장 화면
//    // 파라미터로 넘어오는 roomId를 확인후 해당 roomId를 기준으로 채팅방을 찾아서 클라이언트를 chatroom으로 보낸다.
//    @GetMapping("/chat/{roomId}")
//    public String roomDetail(@PathVariable String roomId,
//                             Model model){
//
//        log.info("roomId {}", roomId);
//        model.addAttribute("room", chatService.findRoomById(roomId));
//        return "chatroom";
//    }


//    @PostMapping
//    public ChatRoom createRoom(@RequestParam Member member1, @RequestParam Member member2) {
//        return chatService.createRoom(member1, member2);
//    }
//
//    @GetMapping
//    public List<ChatRoom> findAllRoom() {
//        return chatService.findAllRoom();
//    }

}
