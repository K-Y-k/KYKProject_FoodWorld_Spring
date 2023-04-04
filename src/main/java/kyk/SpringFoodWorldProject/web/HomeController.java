package kyk.SpringFoodWorldProject.web;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.board.service.BoardServiceImpl;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatMessage;
import kyk.SpringFoodWorldProject.chat.domain.entity.ChatRoom;
import kyk.SpringFoodWorldProject.chat.service.ChatService;
import kyk.SpringFoodWorldProject.member.domain.LoginSessionConst;
import kyk.SpringFoodWorldProject.member.domain.dto.LoginForm;
import kyk.SpringFoodWorldProject.member.domain.dto.MemberSessionDto;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberServiceImpl memberService;
    private final BoardServiceImpl boardService;
    private final ChatService chatService;


    /**
     *  메인 폼 + 로그인 세션 체크 기능 (@SessionAttribute 활용)
     */
    @GetMapping("/")
    public String homeLoginCheck(@SessionAttribute(name = LoginSessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                 @ModelAttribute("loginForm") LoginForm form,
                                 @PageableDefault(page = 0, size = 7, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, 
                                 Model model) {

        Page<Board> freeBoards = boardService.findPageListByBoardType(pageable, "자유게시판");
        Page<Board> recommendBoards = boardService.findPageListByBoardType(pageable, "추천게시판");
        Page<Board> muckstarBoards = boardService.findPageListByBoardType(pageable, "먹스타그램");

        // 댓글 개수 가져오는 작업
        for (Board freeBoard : freeBoards) {
            Board findBoard = boardService.findById(freeBoard.getId()).orElseThrow(() ->
                    new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + freeBoard.getId()));
            boardService.updateCommentCount(findBoard.getId());
        }
        for (Board recommendBoard : recommendBoards) {
            Board findBoard = boardService.findById(recommendBoard.getId()).orElseThrow(() ->
                    new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + recommendBoard.getId()));
            boardService.updateCommentCount(findBoard.getId());
        }
        for (Board muckstarBoard : muckstarBoards) {
            Board findBoard = boardService.findById(muckstarBoard.getId()).orElseThrow(() ->
                    new IllegalArgumentException("게시글 가져오기 실패: 게시글을 찾지 못했습니다." + muckstarBoard.getId()));
            boardService.updateCommentCount(findBoard.getId());
        }

        model.addAttribute("freeBoards", freeBoards);
        model.addAttribute("recommendBoards", recommendBoards);
        model.addAttribute("muckstarBoards", muckstarBoards);
        model.addAttribute("localDateTime", LocalDateTime.now());


        // 채팅방 컨트롤 처리
        if (loginMember != null) {
            List<ChatRoom> member1ChatRoom = chatService.findNotLeaveMessageRoom(loginMember.getId());

            if (!member1ChatRoom.isEmpty()) {
                model.addAttribute("member1ChatRoom", member1ChatRoom);
            }
        }
        
        return "main";
    }


    /**
     * 로그인 기능
     */
    @PostMapping("/")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request) {
        // 검증 실패 : 오류가 있으면 폼으로 반환
        if (bindingResult.hasErrors()) {
            return "main";
        }

        // 성공 로직 : 로그인 기능 적용후 멤버에 저장
        Member loginMember = memberService.login(form.getLoginId(), form.getPassword()); // 폼에 입력한 아이디 패스워드 가져와서 멤버로 저장
        log.info("login? {}", loginMember);

        // null 값이면 로그인 실패 글로벌 오류(오브젝트 오류) 처리 : 글로벌 오류는 DB의 값에서 처리하는 이런 경우도 있어 자바코드로 구현하는게 바람직하다.
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "main";
        }

        // 로그인 성공 처리
        // HttpSession 객체에  request.getSession()로 담으면 됨
        // 세션이 있으면 있는 세션을 반환하고 없으면 신규 세션을 생성한다. (true일 때)
        HttpSession session = request.getSession(); // 기본 값이 true이고, false는 없으면 생성 안함
        // 세션에 로그인 회원 정보를 보관한다.
        session.setAttribute("loginMember", loginMember);

        log.info("sessionId={}", session.getId());
        return "redirect:/";
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
        List<ChatMessage> chatMessage = targetChatRoom.getChatMessages();
        model.addAttribute("chatMessage", chatMessage);

        // 현재 회원의 전체 채팅방 리스트
        List<ChatRoom> member1ChatRoom = chatService.findMember1ChatRoom(loginMember.getId());
        model.addAttribute("member1ChatRoom", member1ChatRoom);

        // 등록한 날이 오늘 날짜이면 시/분까지만 나타나게 조건을 설정하기 위해서 현재 시간을 객체로 담아 보낸 것
        model.addAttribute("localDateTime", LocalDateTime.now());

        return "main";
    }

}
