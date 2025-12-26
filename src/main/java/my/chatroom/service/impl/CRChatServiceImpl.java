package my.chatroom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.chatroom.dao.CRChatDAO;
import my.chatroom.service.CRChatService;
import my.chatroom.vo.CRChatVO;

@Service
public class CRChatServiceImpl implements CRChatService{

	@Autowired
	CRChatDAO crchatDAO;

	@Override
	public int insertChatRoom(CRChatVO chatroom) {
		return crchatDAO.insertChatRoom(chatroom);
	}

	@Override
	public List<CRChatVO> selectChatRoomList(String user1Id) {
		return crchatDAO.selectChatRoomList(user1Id);
	}

	@Override
	public String selectSpecificChatRoom(String user1Id, String user2Id) {
		return crchatDAO.selectSpecificChatRoom(user1Id, user2Id);
	}

	@Override
	public int findTradeId(String roomId) {
		return crchatDAO.findTradeId(roomId);
	}
}
