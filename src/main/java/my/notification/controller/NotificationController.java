package my.notification.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.notification.service.NotiSubscriptionService;
import my.notification.service.NotificationService;
import my.notification.vo.NotiSubscriptionVO;
import my.notification.vo.NotificationVO;
import my.notification.vo.PushSubscription;
import my.notification.vo.SubscriptionVO;
import my.trade.service.TradeService;
import my.trade.vo.TradeVO;
import my.user.service.UserKeywordService;
import my.user.vo.UserKeywordVO;
import my.user.vo.UserVO;

@Controller
public class NotificationController {
	
	@Autowired
	NotificationService notificationService;
	
	@Autowired
	TradeService tradeService;
	
	@Autowired
	UserKeywordService userkeywordService;
	
	@Autowired
	NotiSubscriptionService notisubscriptionService;
	
	String sessionId = null;
	
	@RequestMapping(value="/notificationPage.do", method = RequestMethod.GET)
	public ModelAndView notificationPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("notification.jsp");
		
		HttpSession session = request.getSession();
	    UserVO user = (UserVO)session.getAttribute("USER");
	    String userId = user.getUserId();
		sessionId = userId;
	    
	    List<UserKeywordVO> userkeyword = userkeywordService.findUserKeyword(userId);
	    List<NotificationVO> noti = notificationService.findNotification(userId);
	    List<TradeVO> tradeList = new ArrayList<>();
	    
	    for (NotificationVO notification : noti) {
	        int tradeId = notification.getTradeId();
	        TradeVO trade = tradeService.selectTrade(tradeId);
	        trade.setUserCount(notification.getUserCount());
	        trade.setNotiId(notification.getNotiId());
	        trade.setNow_date(notification.getMakeDate());
	        trade.setNotiStatus(notification.getNotiStatus());
	        tradeList.add(trade);
	    }
	    
	    notificationService.updateNotiIsRead(userId);
	    
	    mav.addObject("userkeyword", userkeyword);
	    mav.addObject("tradeList", tradeList);
	    
	    
		return mav;
	}
	
	@RequestMapping(value="/saveNotiSubscription", method = RequestMethod.POST)
	public ModelAndView saveSubscription(@RequestBody PushSubscription subscriptionvo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		String userId = subscriptionvo.getUserId();
		
	
		SubscriptionVO subscriptionVO = subscriptionvo.getSubscription();
	
		System.out.println(subscriptionVO);
		
		String endpoint = subscriptionVO.getEndpoint(); 
		String p256dhKey = subscriptionVO.getKeys().getP256dh(); 
		String authKey = subscriptionVO.getKeys().getAuth();
		
		
		NotiSubscriptionVO notisub = new NotiSubscriptionVO();
		notisub.setUserId(userId); 
		notisub.setEndpoint(endpoint);
		notisub.setP256dh(p256dhKey); 
		notisub.setAuth(authKey);
		System.out.println(notisub); 
		notisubscriptionService.insertNotiSub(notisub);
		
	    
	    mav.addObject("success", true);	    
	    
		return mav;
	}
	
	@RequestMapping(value="/testingconnect", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView testingconnect(@RequestBody UserVO user) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
	    mav.addObject("useridd", user.getUserId());
	    System.out.println(user.getUserId()); 
	    mav.addObject("userpwdd", user.getPwd());
	    
		return mav;
	}
	
	@RequestMapping(value="/deleteKeyword.do", method = RequestMethod.POST)
	public ModelAndView deleteKeyword(@RequestParam int keywordId) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		userkeywordService.deleteKeyword(keywordId);
	    mav.addObject("success", true);
	   
		return mav;
	}
	
	@RequestMapping(value="/deletenoti.do", method = RequestMethod.POST)
	public ModelAndView deletenoti(@RequestParam int notiId) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		notificationService.deleteNotification(notiId);
	    mav.addObject("success", true);
	   
		return mav;
	}
	
	@RequestMapping(value="/saveKeyword.do", method = RequestMethod.POST)
	public ModelAndView saveKeyword(@RequestParam String userId, @RequestParam String keyword) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		UserKeywordVO userkeyword = new UserKeywordVO();
		userkeyword.setKeyword(keyword);
		userkeyword.setUserId(userId);
		userkeywordService.insertKeyword(userkeyword);
	    mav.addObject("success", true);
	   
		return mav;
	}
}
