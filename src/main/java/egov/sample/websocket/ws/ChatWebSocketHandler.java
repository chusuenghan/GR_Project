package egov.sample.websocket.ws;


//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(EchoHandler.class);
//
//	private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
//
//	@Override
//	public void afterConnectionEstablished(
//			WebSocketSession session) throws Exception {
//		log(session.getId() + " 연결 됨");
//		users.put(session.getId(), session);
//	}
//
//	@Override
//	public void afterConnectionClosed(
//			WebSocketSession session, CloseStatus status) throws Exception {
//		log(session.getId() + " 연결 종료됨");
//		users.remove(session.getId());
//	}
//
//	@Override
//	protected void handleTextMessage(
//			WebSocketSession session, TextMessage message) throws Exception {
//		log(session.getId() + "로부터 메시지 수신: " + message.getPayload());
//		for (WebSocketSession s : users.values()) {
//			s.sendMessage(message);
//			log(s.getId() + "에 메시지 발송: " + message.getPayload());
//		}
//	}
//
//	@Override
//	public void handleTransportError(
//			WebSocketSession session, Throwable exception) throws Exception {
//		log(session.getId() + " 익셉션 발생: " + exception.getMessage());
//	}
//
//	private void log(String logmsg) {
//		LOGGER.info(new Date() + " : " + logmsg);
//	}
//
//}

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class ChatWebSocketHandler extends TextWebSocketHandler{
    //세션 리스트
    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();

    private static Logger logger = LoggerFactory.getLogger(EchoHandler.class);

    //클라이언트가 연결 되었을 때 실행
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionList.add(session);
        logger.info("{} 연결됨", session.getId()); 
    }

    //클라이언트가 웹소켓 서버로 메시지를 전송했을 때 실행
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("{}로 부터 {} 받음", session.getId(), message.getPayload());
        //모든 유저에게 메세지 출력
        for(WebSocketSession sess : sessionList){
            sess.sendMessage(new TextMessage(message.getPayload()));
        }
    }
    //클라이언트 연결을 끊었을 때 실행
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
        logger.info("{} 연결 끊김.", session.getId());
    }
}