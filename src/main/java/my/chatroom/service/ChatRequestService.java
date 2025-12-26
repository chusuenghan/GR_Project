package my.chatroom.service;

import java.util.List;

import my.chatroom.vo.ChatRequestVO;

public interface ChatRequestService {
	public int insertChatRequest(ChatRequestVO chatrequest);
	  
	  public String checkChatRequestStatus(ChatRequestVO chatrequest);
	  
	  public int declineChatRequest(ChatRequestVO chatrequest);
	  
	  public String getChatRequestStatus(ChatRequestVO chatrequest);
	  
	  public List<ChatRequestVO> getChatRequests(int tradeId);
	  
	  public int acceptChatRequest(ChatRequestVO chatrequest);
	  
	  public List<Integer> findAcceptedRoom(String requesterId);
	  
	  public int declineChatPending(int tradeId);
	  
	  public int declineChatAccept(int tradeId);
}
