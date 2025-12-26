package my.chatroom.service;

import java.util.List;

import my.chatroom.vo.ChatRoomVO;

public interface ChatRoomService {
	public int insertChat(ChatRoomVO chatroom);
	public List<ChatRoomVO> selectChatRoom(String roomId);
}
