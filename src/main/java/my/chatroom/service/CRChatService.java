package my.chatroom.service;

import java.util.List;

import my.chatroom.vo.CRChatVO;

public interface CRChatService {
	public int insertChatRoom(CRChatVO chatroom);
	public List<CRChatVO> selectChatRoomList(String user1Id);
	public String selectSpecificChatRoom(String user1Id, String user2Id);
	public int findTradeId(String roomId);
}
