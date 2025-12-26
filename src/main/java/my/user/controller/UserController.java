package my.user.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import my.chatroom.controller.ChatRoomController;
import my.chatroom.service.CRChatService;
import my.chatroom.service.ChatRequestService;
import my.chatroom.vo.CRChatVO;
import my.chatroom.vo.ChatRequestVO;
import my.notification.service.NotiSubscriptionService;
import my.notification.service.NotificationService;
import my.notification.vo.NotiSubscriptionVO;
import my.notification.vo.NotificationVO;
import my.trade.service.TradeService;
import my.trade.vo.TradeVO;
import my.user.service.UserKeywordService;
import my.user.service.UserRatingService;
import my.user.service.UserService;
import my.user.vo.UserKeywordVO;
import my.user.vo.UserRatingVO;
import my.user.vo.UserVO;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserRatingService userratingService;
	
	@Autowired
	TradeService tradeService;
	
	@Autowired
	ChatRequestService chatrequestService;
	
	@Autowired
	CRChatService crchatService;
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	UserKeywordService userkeywordService;
		
	@Autowired
	NotificationService notificationService;
		
	@Autowired
	NotiSubscriptionService notisubscriptionService;
	  
	@RequestMapping(value="/signUpPage.do", method = RequestMethod.GET)
	public ModelAndView signUpPage() {
		ModelAndView mav = new ModelAndView("user/signUp.jsp");
		
		return mav;
	}
	
	@RequestMapping(value="/signUp.do", method = RequestMethod.POST)
	public String signUp(@ModelAttribute UserVO user) {
//		UserVO uservo = userService.selectUserInfo(user.getUserId());
//		if(uservo.getUserId() != null || uservo.getUserId().length() != 0) {
//			userService.insertUser(user);
//			return "redirect:/loginPage.do";
//		}
		
		userService.insertUser(user);
		return "redirect:/loginPage.do";
	}
	
	@RequestMapping(value="/pwdConfirmPage.do", method = RequestMethod.GET)
	public String pwdConfirmPage() {
		return "user/pwdConfirm.jsp";
	}
	
	@RequestMapping(value="/userUpdatePage.do", method = RequestMethod.POST)
	public ModelAndView userUpdatePage(@ModelAttribute UserVO user) {
		if(userService.selectPwd(user.getUserId(), user.getPwd())) {
			ModelAndView mav = new ModelAndView("user/userUpdate.jsp");
			
			return mav;
		} else {
			ModelAndView mav = new ModelAndView("main.jsp");
			return mav;
		}
	}
	
	@RequestMapping(value="/userUpdate.do", method = RequestMethod.POST)
	public String updateUser(HttpSession session, @ModelAttribute UserVO user) {
		userService.updateUser(user);
		userService.setSession(session, user.getUserId());
		return "main.jsp";
	}
	
	@RequestMapping(value="/userDelete.do", method = RequestMethod.GET)
	public String userDelete(HttpSession session) {
		UserVO userInfo = (UserVO) session.getAttribute("USER");
		
//		System.out.println(userInfo.getUserId());
		userService.deleteUser(userInfo.getUserId());
		session.removeAttribute("USER");
		
		return "redirect:/loginPage.do";
	}
	
	@RequestMapping(value="/myPage.do", method = RequestMethod.GET)
	public ModelAndView myPage(HttpSession session) {
		ModelAndView mav = new ModelAndView("mypage.jsp");
		UserVO userInfo = (UserVO) session.getAttribute("USER");
		String writerId = userInfo.getUserId();
		UserRatingVO rating = userratingService.avgRating(writerId);
		List<TradeVO> tradeList = tradeService.selectMyTrade(writerId);
		System.out.println(rating);
		List<Integer> findRoomId = chatrequestService.findAcceptedRoom(writerId);
		List<TradeVO> rateList = new ArrayList<>();
		if (!findRoomId.isEmpty()) {
		    rateList = tradeService.selectByTradeIds(findRoomId);
		}
		mav.addObject("tradeList", tradeList);
		mav.addObject("rateList", rateList);
		mav.addObject("rating", rating);
		
		return mav;
	}
	
	@RequestMapping(value="/getChatRequests.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getChatRequests(@RequestParam int tradeId) {
		ModelAndView mav = new ModelAndView("jsonView");
		
		List<ChatRequestVO> requestIdList = chatrequestService.getChatRequests(tradeId);
		mav.addObject("requestIdList", requestIdList);
		
		return mav;
	}
	
	@RequestMapping(value="/acceptChatRequest.do", method = RequestMethod.POST)
	public ModelAndView acceptChatRequest(@RequestParam String requesterId, @RequestParam int tradeId, @RequestParam String writerId) {
		ModelAndView mav = new ModelAndView("jsonView");
		
		ChatRequestVO chatrequest = new ChatRequestVO();
		chatrequest.setRequesterId(requesterId);
		chatrequest.setTradeId(tradeId);
		
		TradeVO trade = new TradeVO();
		trade.setTradeId(tradeId);
		trade.setTradeStatus("MATCH");
		try {
	    	chatrequestService.acceptChatRequest(chatrequest);
	    	UUID uuid = UUID.randomUUID();
			String newroom = uuid.toString();
			CRChatVO createroom = new CRChatVO();
			createroom.setUser1Id(requesterId);
			createroom.setUser2Id(writerId);
			createroom.setRoomId(newroom);
			createroom.setTradeId(tradeId);
			crchatService.insertChatRoom(createroom);
			tradeService.updateTradeStatus(trade);
	        mav.addObject("success", true);
	        mav.addObject("message", "Chat request accepted successfully!");
	        
	        TradeVO tradevo = tradeService.selectTrade(tradeId);
	        
	        List<String> usersToNotify = new ArrayList<>();
	  		List<UserKeywordVO>userkeyword = userkeywordService.findTitleKeyword();
	  		
	  		for (UserKeywordVO Keyword : userkeyword) {
	  	        if (tradevo.getTitle().contains(Keyword.getKeyword())) {
	  	        	if(!Keyword.getUserId().equals(tradevo.getWriterId()) && !Keyword.getUserId().equals(requesterId))
	  	        		usersToNotify.add(Keyword.getUserId());
	  	        }
	  	    }
	  		
	  		Set<String> uniqueUsersToNotify = new HashSet<>(usersToNotify);
	  		
	  		int noticount = uniqueUsersToNotify.size();
	  		tradevo.setUserCount(noticount);
	  		
	  		for(String userId : uniqueUsersToNotify) {
	  			NotificationVO noti = new NotificationVO();
	  			noti.setUserId(userId);
	  			noti.setTradeId(tradeId);
	  			noti.setUserCount(noticount);
	  			noti.setNotiStatus("MA");
	  			tradevo.setNotiStatus("MA");
	  			notificationService.insertNotification(noti);
	  			simpMessagingTemplate.convertAndSend("/topic/notifications/" + userId, "메시지가 있습니다.");
	  			List<NotiSubscriptionVO> notisub = notisubscriptionService.findUserNotiSub(userId);
	  			if(notisub.size() != 0) {
	  				for(NotiSubscriptionVO sub : notisub) {
	  					sendWebPush(sub, tradevo, userId);
	  				}
	  			}
	        }
	  		
	  		NotificationVO notivo = new NotificationVO();
	  		notivo.setUserId(requesterId);
	  		notivo.setTradeId(tradeId);
	  		notivo.setNotiStatus("MM");
	  		tradevo.setNotiStatus("MM");
  			notificationService.insertNotification(notivo);
  			simpMessagingTemplate.convertAndSend("/topic/notifications/" + requesterId, "메시지가 있습니다.");
  			List<NotiSubscriptionVO> notisubvo = notisubscriptionService.findUserNotiSub(requesterId);
  			if(notisubvo.size() != 0) {
  				for(NotiSubscriptionVO subvo : notisubvo) {
  					sendWebPush(subvo, tradevo, requesterId);
  				}
  			}
	  		
	  		

	    } catch(Exception e) {
	        mav.addObject("success", false);
	        mav.addObject("message", "Failed to accepted chat request.");
	    }
		
		return mav;
	}
	
	@RequestMapping(value="/declineChatRequest.do", method = RequestMethod.POST)
	public ModelAndView declineChatRequest(@RequestParam String requesterId, @RequestParam int tradeId) {
		ModelAndView mav = new ModelAndView("jsonView");
		
		ChatRequestVO chatrequest = new ChatRequestVO();
		chatrequest.setRequesterId(requesterId);
		chatrequest.setTradeId(tradeId);
		try {
	    	chatrequestService.declineChatRequest(chatrequest);
	        mav.addObject("success", true);
	        mav.addObject("message", "Chat request declined successfully!");
	    } catch(Exception e) {
	        mav.addObject("success", false);
	        mav.addObject("message", "Failed to decline chat request.");
	    }
		
		return mav;
	}
	
	@RequestMapping(value="/updateTradeStatus.do", method = RequestMethod.POST)
	public ModelAndView updateTradeStatus(@RequestParam int tradeId, @RequestParam String status) {
		ModelAndView mav = new ModelAndView("jsonView");
		TradeVO trade = new TradeVO();
		trade.setTradeId(tradeId);
		trade.setTradeStatus(status);
		
		try {
	    	tradeService.updateTradeStatus(trade);
	    	if(status.equals("FIND"))
	    	{
	    		chatrequestService.declineChatAccept(tradeId);
	    		mav.addObject("success", true);
		        mav.addObject("message", "Chat accept declined successfully!");
		        return mav;
	    	}
	    	chatrequestService.declineChatPending(tradeId);
	        mav.addObject("success", true);
	        mav.addObject("message", "Chat request declined successfully!");
	        
	        TradeVO tradevo = tradeService.selectTrade(tradeId);
	        
	        List<String> usersToNotify = new ArrayList<>();
	  		List<UserKeywordVO>userkeyword = userkeywordService.findTitleKeyword();
	  		
	  		for (UserKeywordVO Keyword : userkeyword) {
	  	        if (tradevo.getTitle().contains(Keyword.getKeyword())) {
	  	        	if(!Keyword.getUserId().equals(tradevo.getWriterId()))
	  	        		usersToNotify.add(Keyword.getUserId());
	  	        }
	  	    }
	  		
	  		Set<String> uniqueUsersToNotify = new HashSet<>(usersToNotify);
	  		
	  		int noticount = uniqueUsersToNotify.size();
	  		tradevo.setUserCount(noticount);
	  		
	  		for(String userId : uniqueUsersToNotify) {
	  			NotificationVO noti = new NotificationVO();
	  			noti.setUserId(userId);
	  			noti.setTradeId(tradeId);
	  			noti.setUserCount(noticount);
	  			noti.setNotiStatus("EN");
	  			tradevo.setNotiStatus("EN");
	  			notificationService.insertNotification(noti);
	  			simpMessagingTemplate.convertAndSend("/topic/notifications/" + userId, "메시지가 있습니다.");
	  			List<NotiSubscriptionVO> notisub = notisubscriptionService.findUserNotiSub(userId);
	  			if(notisub.size() != 0) {
	  				for(NotiSubscriptionVO sub : notisub) {
	  					sendWebPush(sub, tradevo, userId);
	  				}
	  			}
	        }
	    } catch(Exception e) {
	        mav.addObject("success", false);
	        mav.addObject("message", "Failed to decline chat request.");
	    }
		
		return mav;
	}
	
	@RequestMapping(value="/submitRating.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView submitRating(@RequestParam int rating,@RequestParam int tradeId) {
		ModelAndView mav = new ModelAndView("jsonView");
		String writerId = tradeService.selectWriterId(tradeId);
		UserRatingVO userrating = new UserRatingVO();
		userrating.setUserId(writerId);
		userrating.setRating(rating);
		TradeVO trade = new TradeVO();
		trade.setTradeId(tradeId);
		trade.setTradeStatus("END");
		
		try {
			int userRate = userratingService.insertRating(userrating);
			tradeService.updateTradeStatus(trade);
	        mav.addObject("success", true);
	        mav.addObject("message", "Chat request declined successfully!");
	    } catch(Exception e) {
	        mav.addObject("success", false);
	        mav.addObject("message", "Failed to decline chat request.");
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
	        String payload;
	        if(trade.getNotiStatus().equals("MA")) {
	        payload = "{"
	            + "\"title\": \"" + userId + "님에게 알림 메세지\","
	            + "\"body\": \"" + trade.getTitle() + "\\n게시물이 타인과 매칭되었습니다.\""
	            + "}";
	        }
	        else if(trade.getNotiStatus().equals("MM")) {
		        payload = "{"
		            + "\"title\": \"" + userId + "님에게 알림 메세지\","
		            + "\"body\": \"" + trade.getTitle() + "\\n게시물과 매칭되었습니다.\""
		            + "}";
		        }
	        else {
		        payload = "{"
		            + "\"title\": \"" + userId + "님에게 알림 메세지\","
		            + "\"body\": \"" + trade.getTitle() + "\\n게시물이 거래 완료 되었습니다.\""
		            + "}";
		        }
	        System.out.println("pay load: " + payload);
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
}