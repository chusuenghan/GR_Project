package egov.sample.websocket.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import egov.sample.websocket.ws.ChatWebSocketHandler;
import egov.sample.websocket.ws.EchoHandler;

@Configuration
@EnableWebSocket
public class WsConfig implements WebSocketConfigurer {
	

	/*
	 * @Override public void configureMessageBroker(MessageBrokerRegistry config) {
	 * config.enableSimpleBroker("/topic"); // topic으로 시작하는 주제에 메시지를 브로커가 전송
	 * config.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 send 요청을 할 때
	 * 사용하는 prefix }
	 * 
	 * @Override public void registerStompEndpoints(StompEndpointRegistry registry)
	 * { registry.addEndpoint("/chat-ws").withSockJS(); // SockJS 기반 STOMP 엔드포인트 등록
	 * registry.addEndpoint("/echo-ws").withSockJS(); }
	 */
	
	@Override

	 public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {



	  registry.addHandler(chatHandler(), "/chat-ws.do").withSockJS();
	  registry.addHandler(echoHandler(), "/echo-ws.do");

	 }



	 

	 @Bean

	 public ChatWebSocketHandler chatHandler() {

	  return new ChatWebSocketHandler();

	 }

	 @Bean

	 public EchoHandler echoHandler() {

	  return new EchoHandler();

	 }


}
