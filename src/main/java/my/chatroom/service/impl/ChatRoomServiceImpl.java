package my.chatroom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.chatroom.dao.ChatRoomDAO;
import my.chatroom.service.ChatRoomService;
import my.chatroom.vo.ChatRoomVO;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{
	
	@Autowired
	ChatRoomDAO chatroomDAO;

	@Override
	public int insertChat(ChatRoomVO chatroom) {
		return chatroomDAO.insertChat(chatroom);
	}

	@Override
	public List<ChatRoomVO> selectChatRoom(String roomId) {
		return chatroomDAO.selectChatRoom(roomId);
	}
}
