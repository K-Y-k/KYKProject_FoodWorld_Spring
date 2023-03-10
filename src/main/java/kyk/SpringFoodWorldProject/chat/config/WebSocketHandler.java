//package kyk.SpringFoodWorldProject.chat.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kyk.SpringFoodWorldProject.chat.domain.dto.ChatMessageDto;
//import kyk.SpringFoodWorldProject.chat.domain.dto.ChatRoomDto;
//import kyk.SpringFoodWorldProject.chat.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
///**
// * 웹 소켓 통신은 서버와 클라이언트가 1:N 관계
// * 따라서 한 서버에 여러 클라이언트가 접속 할 수 있으며,
// * 서버에는 여러 클라이언트가 발송한 메시지를 받아 처리해줄 Handler가 필요하다.
// * TextWebSocketHandler를 상속받은 Handler를 작성
// * 클라이언트로부터 받은 메시지를 콘솔에 출력하고 클라이언트로 환영 메시지를 보내는 역할을 한다.
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper; // 객체를 Json형식의 문자열로 바꾸어줌
//    private final ChatService chatService;
//
//    /**
//     * 소켓 연결 후 동작 메서드
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//    }
//
//
//    /**
//     * 메시지 발송 : 메시지 수신 후 동작하는 메서드
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload(); // 패킷 단위의 페이로드를 담음
//        log.info("채팅 로그 = {}", payload);
//
//        TextMessage textMessage = new TextMessage("kyk 웹소켓 테스트");
//        session.sendMessage(textMessage);
//
////        ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
////        ChatRoomDto room = chatService.findRoomById(chatMessage.getChatRoom().getChatRoomId());
////        room.handleActions(session, chatMessage, chatService);
//    }
//
//
//    /**
//     * 소켓 종료 후 동작 메서드
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//
//    }
//
//}
