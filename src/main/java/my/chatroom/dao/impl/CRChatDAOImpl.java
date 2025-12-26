package my.chatroom.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.chatroom.dao.CRChatDAO;
import my.chatroom.vo.CRChatVO;

@Repository
public class CRChatDAOImpl extends EgovAbstractMapper implements CRChatDAO{

	@Override
	public int insertChatRoom(CRChatVO chatroom) {
		return insert("CRChat.insertChatRoom", chatroom);
	}

	@Override
	public List<CRChatVO> selectChatRoomList(String user1Id) {
		return selectList("CRChat.selectChatRoomList", user1Id);
	}

	@Override
	public String selectSpecificChatRoom(String user1Id, String user2Id) {
		Map<String, String> params = new HashMap<>();
		params.put("user1Id", user1Id);
		params.put("user2Id", user2Id);
		return selectOne("CRChat.selectSpecificChatRoom", params);
	}

	@Override
	public int findTradeId(String roomId) {
		return selectOne("CRChat.findTradeId", roomId);
	}

	
}
