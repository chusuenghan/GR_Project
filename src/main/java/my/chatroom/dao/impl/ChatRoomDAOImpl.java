package my.chatroom.dao.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.chatroom.dao.ChatRoomDAO;
import my.chatroom.vo.ChatRoomVO;

@Repository
public class ChatRoomDAOImpl extends EgovAbstractMapper implements ChatRoomDAO{
	
	@Override
	public int insertChat(ChatRoomVO chatroom) {
		return insert("ChatRoom.insertChat", chatroom);
	}
	
	@Override
	public List<ChatRoomVO> selectChatRoom(String roomId){
		return selectList("ChatRoom.selectChatRoom", roomId);
	}

}
