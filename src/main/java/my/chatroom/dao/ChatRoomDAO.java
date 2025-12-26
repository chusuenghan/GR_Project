package my.chatroom.dao;

import java.util.List;

import my.chatroom.vo.ChatRoomVO;


public interface ChatRoomDAO {
	public int insertChat(ChatRoomVO chatroom);
	public List<ChatRoomVO> selectChatRoom(String roomId);
}
