package my.chatroom.dao.impl;

import java.util.List;
import my.chatroom.dao.ChatRequestDAO;
import my.chatroom.vo.ChatRequestVO;
import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRequestDAOImpl extends EgovAbstractMapper implements ChatRequestDAO
{
	@Override
	public int insertChatRequest(ChatRequestVO chatrequest)
	{
	  return insert("chatRequest.insertChatRequest", chatrequest);
	}
  @Override
  public String checkChatRequestStatus(ChatRequestVO chatrequest)
  {
    return selectOne("chatRequest.checkChatRequestStatus", chatrequest);
  }
  
  @Override
  public int declineChatRequest(ChatRequestVO chatrequest)
  {
    return delete("chatRequest.declineChatRequest", chatrequest);
  }
  
  @Override
  public String getChatRequestStatus(ChatRequestVO chatrequest)
  {
    return selectOne("chatRequest.getChatRequestStatus", chatrequest);
  }
  
  @Override
  public List<ChatRequestVO> getChatRequests(int tradeId)
  {
    return selectList("chatRequest.getChatRequests", tradeId);
  }
  
  @Override
  public int acceptChatRequest(ChatRequestVO chatrequest)
  {
    return update("chatRequest.acceptChatRequest", chatrequest);
  }
  
  @Override
  public List<Integer> findAcceptedRoom(String requesterId)
  {
    return selectList("chatRequest.findAcceptedRoom", requesterId);
  }
  
  @Override
  public int declineChatPending(int tradeId)
  {
    return delete("chatRequest.declineChatPending", tradeId);
  }
@Override
public int declineChatAccept(int tradeId) {
	return delete("chatRequest.declineChatAccept", tradeId);
}
}
