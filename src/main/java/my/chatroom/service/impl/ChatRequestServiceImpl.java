package my.chatroom.service.impl;

import java.util.List;
import my.chatroom.dao.ChatRequestDAO;
import my.chatroom.service.ChatRequestService;
import my.chatroom.vo.ChatRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRequestServiceImpl implements ChatRequestService
{
  @Autowired
  ChatRequestDAO chatrequestDAO;
  
  @Override
  public int insertChatRequest(ChatRequestVO chatrequest)
  {
    return chatrequestDAO.insertChatRequest(chatrequest);
  }
  
  @Override
  public String checkChatRequestStatus(ChatRequestVO chatrequest)
  {
    return chatrequestDAO.checkChatRequestStatus(chatrequest);
  }
  
  @Override
  public int declineChatRequest(ChatRequestVO chatrequest)
  {
    return chatrequestDAO.declineChatRequest(chatrequest);
  }
  
  @Override
  public String getChatRequestStatus(ChatRequestVO chatrequest)
  {
    return chatrequestDAO.getChatRequestStatus(chatrequest);
  }
  
  @Override
  public List<ChatRequestVO> getChatRequests(int tradeId)
  {
    return chatrequestDAO.getChatRequests(tradeId);
  }
  
  @Override
  public int acceptChatRequest(ChatRequestVO chatrequest)
  {
    return chatrequestDAO.acceptChatRequest(chatrequest);
  }
  
  @Override
  public List<Integer> findAcceptedRoom(String requesterId)
  {
    return chatrequestDAO.findAcceptedRoom(requesterId);
  }
  
  @Override
  public int declineChatPending(int tradeId)
  {
    return chatrequestDAO.declineChatPending(tradeId);
  }

@Override
public int declineChatAccept(int tradeId) {
	return chatrequestDAO.declineChatAccept(tradeId);
}
}
