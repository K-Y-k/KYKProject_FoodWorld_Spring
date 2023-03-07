package kyk.SpringFoodWorldProject.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 웹 소켓 통신은 서버와 클라이언트가 1:N 관계
 * 따라서 한 서버에 여러 클라이언트가 접속 할 수 있으며,
 * 서버에는 여러 클라이언트가 발송한 메시지를 받아 처리해줄 Handler의 작성이 필요하다.
 * TextWebSocketHandler를 상속받은 Handler를 작성
 * 클라이언트로부터 받은 메시지를 콘솔에 출력하고 클라이언트로 환영 메시지를 보내는 역할을 한다.
 */
@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String input = message.getPayload();
        log.info("채팅 로그 = {}", input); // 채팅 log
        TextMessage textMessage = new TextMessage("kyk \n 웹소켓 테스트");
        session.sendMessage(textMessage);
    }
}
