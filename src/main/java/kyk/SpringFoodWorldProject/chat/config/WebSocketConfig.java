package kyk.SpringFoodWorldProject.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration // 스프링 컨테이너에 빈으로 등록
@EnableWebSocket // 웹 소켓을 사용하기위한 어노테이션
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketHandler webSocketHandler; // 만들었던 핸들러를 주입

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(webSocketHandler, "/chat").setAllowedOrigins("*");
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); //   /app으로 시작하는 메시지가 메세지를 처리하는 메서드(message-handling methods)로 라우팅 되어야 한다고 정의
        registry.enableSimpleBroker("/chat"); // /chat으로 시작하는 메시지가 메세지 브로커로 라우팅 되어야 한다고 정의
    }
}
