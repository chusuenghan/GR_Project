package my.chatroom.controller;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import my.chatroom.service.CRChatService;
import my.chatroom.service.ChatRequestService;
import my.chatroom.service.ChatRoomService;
import my.chatroom.vo.CRChatVO;
import my.chatroom.vo.ChatRequestVO;
import my.chatroom.vo.ChatRoomVO;
import my.notification.service.NotiSubscriptionService;
import my.notification.service.NotificationService;
import my.notification.vo.NotiSubscriptionVO;
import my.notification.vo.NotificationVO;
import my.trade.controller.TradeController;
import my.trade.service.TradeService;
import my.trade.vo.TradeVO;
import my.user.service.UserKeywordService;
import my.user.vo.UserKeywordVO;
import my.user.vo.UserVO;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChatRoomController
{
	private static final Logger logger = LoggerFactory.getLogger(ChatRoomController.class);
  @Autowired
  ChatRoomService chatroomService;
  
  @Autowired
  CRChatService crchatService;
  
  @Autowired
  ChatRequestService chatrequestService;
  
  @Autowired
  TradeService tradeService;
  
  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  UserKeywordService userkeywordService;
	
  @Autowired
  NotificationService notificationService;
	
  @Autowired
  NotiSubscriptionService notisubscriptionService;
	
  String sesId = null;
  
  @MessageMapping({"/chat/{targetroomId}"})
  @SendTo({"/topic/chat/{targetroomId}"})
  public ChatRoomVO chat(@DestinationVariable String targetroomId, ChatRoomVO message)
    throws Exception
  {
    System.out.println(message);
    this.chatroomService.insertChat(message);
    
    String generatedChatTime = message.getChatTime();
    if (generatedChatTime != null)
    {
      SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
      
      Date date = originalFormat.parse(generatedChatTime);
      String formattedDate = targetFormat.format(date);
      message.setChatTime(formattedDate);
    }
    else
    {
      Date now = new Date();
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
      
      String formattedDate = formatter.format(now);
      message.setChatTime(formattedDate);
    }
    return message;
  }
  
  @RequestMapping(value="/stomp_chat.do", method= RequestMethod.GET)
  public String stompChat()
  {
    return "chat_room.jsp";
  }
  
  @RequestMapping(value="/chatList.do", method=RequestMethod.GET)
  @ResponseBody
  public ModelAndView chatList(HttpServletRequest request)
  {
    ModelAndView mav = new ModelAndView("jsonView");
    
    HttpSession session = request.getSession();
    UserVO user = (UserVO)session.getAttribute("USER");
    String user1Id = user.getUserId();
    if (sesId == null) {
      sesId = user1Id;
    }
    System.out.println(user1Id + " hello");
    List<CRChatVO> chatList = crchatService.selectChatRoomList(user1Id);
    
    mav.addObject("chatList", chatList);
    
    return mav;
  }
  
  @RequestMapping(value="/chatLog.do/{targetroomId}", method=RequestMethod.GET)
  @ResponseBody
  public ModelAndView showChat(@PathVariable("targetroomId") String roomId)
  {
    ModelAndView mav = new ModelAndView("jsonView");
    
    List<ChatRoomVO> chatLog = this.chatroomService.selectChatRoom(roomId);
    int tradeId = this.crchatService.findTradeId(roomId);
    TradeVO trade = this.tradeService.selectTrade(tradeId);
    
    mav.addObject("chatLog", chatLog);
    mav.addObject("trade", trade);
    return mav;
  }
  
  @RequestMapping("/createchat.do")
  public ModelAndView createChat(@RequestParam String user1Id, @RequestParam String user2Id)
  {
    ModelAndView mav = new ModelAndView("chat_room.jsp");
    String checkingchat = this.crchatService.selectSpecificChatRoom(user1Id, user2Id);
    if (checkingchat == null)
    {
      UUID uuid = UUID.randomUUID();
      String newroom = uuid.toString();
      CRChatVO createroom = new CRChatVO();
      createroom.setUser1Id(user1Id);
      createroom.setUser2Id(user2Id);
      createroom.setRoomId(newroom);
      int i = this.crchatService.insertChatRoom(createroom);
    }
    return mav;
  }
  
  @RequestMapping(value="/sendChatRequest.do", method=RequestMethod.POST)
  public ModelAndView sendChatRequest(@RequestParam int tradeId, @RequestParam String requesterId, @RequestParam String writerId)
  {
    ModelAndView mav = new ModelAndView("jsonView");
    try
    {
      ChatRequestVO chatRequest = new ChatRequestVO();
      chatRequest.setRequesterId(requesterId);
      chatRequest.setTradeId(tradeId);
      chatRequest.setWriterId(writerId);
      chatrequestService.insertChatRequest(chatRequest);
      mav.addObject("status", "success");
      mav.addObject("message", "대화 요청을 성공했습니다!");
      System.out.println(tradeId + " : " + writerId + " : " + requesterId);
      TradeVO trade = tradeService.selectTrade(chatRequest.getTradeId());
      
      List<String> usersToNotify = new ArrayList<>();
		List<UserKeywordVO>userkeyword = userkeywordService.findTitleKeyword();
		
		for (UserKeywordVO Keyword : userkeyword) {
		    if (trade.getTitle().contains(Keyword.getKeyword()) 
		        && !Keyword.getUserId().equals(requesterId) 
		        && !Keyword.getUserId().equals(writerId)) {
		            usersToNotify.add(Keyword.getUserId());
		    }
		}
		
		Set<String> uniqueUsersToNotify = new HashSet<>(usersToNotify);
		
		int noticount = uniqueUsersToNotify.size();
		trade.setUserCount(noticount);
		
		for(String userId : uniqueUsersToNotify) {
			if(userId.equals(requesterId)) continue;
			NotificationVO noti = new NotificationVO();
			noti.setUserId(userId);
			noti.setTradeId(chatRequest.getTradeId());
			noti.setUserCount(noticount);
			noti.setNotiStatus("RE");
			notificationService.insertNotification(noti);
			simpMessagingTemplate.convertAndSend("/topic/notifications/" + userId, "메시지가 있습니다.");
			List<NotiSubscriptionVO> notisub = notisubscriptionService.findUserNotiSub(userId);
			if(notisub.size() != 0) {
				for(NotiSubscriptionVO sub : notisub) {
					sendWebPush(sub, trade, userId);
				}
			}
      }
		NotificationVO notivo = new NotificationVO();
		notivo.setTradeId(chatRequest.getTradeId());
		notivo.setUserId(chatRequest.getWriterId());
		notivo.setNotiStatus("MY");
		notificationService.insertNotification(notivo);
		simpMessagingTemplate.convertAndSend("/topic/notifications/" + chatRequest.getWriterId(), "메시지가 있습니다.");
		List<NotiSubscriptionVO> notisubvo = notisubscriptionService.findUserNotiSub(chatRequest.getWriterId());
		if(notisubvo.size() != 0) {
			for(NotiSubscriptionVO sub : notisubvo) {
				sendMyPush(sub, trade, chatRequest.getWriterId());
			}
		}
    }
    catch (Exception e)
    {
      mav.addObject("status", "error");
      mav.addObject("message", "대화 요청을 실패했습니다.");
    }
    return mav;
  }
  public void sendWebPush(NotiSubscriptionVO subscription, TradeVO trade, String userId) {
		//Security.addProvider(new BouncyCastleProvider());
	    try {
	    	logger.info("try 블록 시작.");
	        PushService pushService = new PushService(
	        		"BDoPnG6xdPyxwyzX-EQLZh8DRKfkT5jfC95knX47jyLecsZ0JLTbN0iKBrzesfPq6F_nWl7XnHb_hK-ohOEM01M",
	        		"qLYBBH1Qd7ax6uZUVFlpeU7L1LSSa_kHRpGYzIimqJU");
	        logger.info("push 블록 생성.");
	        // 알림 내용 구성
	        
	        String payload = "{"
	            + "\"title\": \"" + userId + "님에게 알림 메세지\","
	            + "\"body\": \"" + trade.getTitle() + "\\n게시물에 타인이 대화요청을 했습니다.\","
	            + "\"url\": \"http://localhost:8080/TestingFile/tradeInfoPage/" + trade.getTradeId() + ".do\""
	            + "}";
	        // 웹 푸시 알림 생성 및 전송
	        byte[] payloadBytes = payload.getBytes("UTF-8");
	        String publickey = subscription.getP256dh();
	        String authkey = subscription.getAuth();
	        String endpoint = subscription.getEndpoint();
	        
	        Notification pushMessage = new Notification(
	        		subscription.getEndpoint(),
	        		subscription.getUserPublicKey(),
	        		subscription.getAuthAsBytes(),
	        		payloadBytes
	        	    );
	        logger.info("noti 블록 생성.");
	        HttpResponse httpResponse = pushService.send(pushMessage);
	        int statusCode = httpResponse.getStatusLine().getStatusCode();
	        System.out.println("상태 메시지: " + statusCode);
	        
	        if (statusCode != 201) {
	            System.out.println("status 오류");
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	        System.out.println("예외 메시지: " + e.getMessage());
	    }
	}
  public void sendMyPush(NotiSubscriptionVO subscription, TradeVO trade, String userId) {
		//Security.addProvider(new BouncyCastleProvider());
	    try {
	    	logger.info("try 블록 시작.");
	        PushService pushService = new PushService(
	        		"BDoPnG6xdPyxwyzX-EQLZh8DRKfkT5jfC95knX47jyLecsZ0JLTbN0iKBrzesfPq6F_nWl7XnHb_hK-ohOEM01M",
	        		"qLYBBH1Qd7ax6uZUVFlpeU7L1LSSa_kHRpGYzIimqJU");
	        logger.info("push 블록 생성.");
	        // 알림 내용 구성
	        
	        String payload = "{"
	            + "\"title\": \"" + userId + "님에게 알림 메세지\","
	            + "\"body\": \"" + trade.getTitle() + "\\n게시물에 대화요청이 왔습니다.\""
	            + "}";
	        // 웹 푸시 알림 생성 및 전송
	        byte[] payloadBytes = payload.getBytes("UTF-8");
	        String publickey = subscription.getP256dh();
	        String authkey = subscription.getAuth();
	        String endpoint = subscription.getEndpoint();
	        
	        Notification pushMessage = new Notification(
	        		subscription.getEndpoint(),
	        		subscription.getUserPublicKey(),
	        		subscription.getAuthAsBytes(),
	        		payloadBytes
	        	    );
	        logger.info("noti 블록 생성.");
	        HttpResponse httpResponse = pushService.send(pushMessage);
	        int statusCode = httpResponse.getStatusLine().getStatusCode();
	        System.out.println("상태 메시지: " + statusCode);
	        
	        if (statusCode != 201) {
	            System.out.println("status 오류");
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	        System.out.println("예외 메시지: " + e.getMessage());
	    }
	}
  
  
  @RequestMapping("/checkChatRequestStatus.do")
  public ModelAndView checkChatRequestStatus(@ModelAttribute ChatRequestVO chatRequest)
  {
    ModelAndView mav = new ModelAndView("jsonView");
    
    String result = chatrequestService.checkChatRequestStatus(chatRequest);
    if (result != null) {
      mav.addObject("status", result);
    } else {
      mav.addObject("error", "Chat request not found.");
    }
    return mav;
  }
}