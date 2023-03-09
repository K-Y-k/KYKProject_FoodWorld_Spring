package kyk.SpringFoodWorldProject.chat.controller;

import kyk.SpringFoodWorldProject.chat.domain.dto.ChatRoomDto;
import kyk.SpringFoodWorldProject.chat.service.ChatService;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService chatService;


    // 채팅 리스트 화면
    // 요청이 들어오면 전체 채팅룸 리스트를 담아서 return
    @GetMapping("/chat/{memberId}")
    public String goChatRoom(@PathVariable Long memberId,
                             Model model){

        model.addAttribute("list", chatService.findAllRoom());

        log.info("SHOW ALL ChatList {}", chatService.findAllRoom());
        return "chat/chat";
    }


    // 채팅방 생성 : 채팅방 생성 후 다시 return
    @PostMapping("/chat/createroom/{memberId}")
    public String createRoom(@RequestParam String name, RedirectAttributes redirectAttributes) {
        ChatRoomDto room = chatService.createRoom(name);
        log.info("CREATE Chat Room {}", room);

        redirectAttributes.addFlashAttribute("roomName", room);
        return "redirect:/chat";
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId를 확인후 해당 roomId를 기준으로 채팅방을 찾아서 클라이언트를 chatroom으로 보낸다.
    @GetMapping("/chat/{roomId}")
    public String roomDetail(@PathVariable String roomId,
                             Model model){

        log.info("roomId {}", roomId);
        model.addAttribute("room", chatService.findRoomById(roomId));
        return "chatroom";
    }


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
