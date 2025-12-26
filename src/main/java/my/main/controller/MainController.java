package my.main.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.user.service.UserService;
import my.user.vo.UserVO;

@Controller
public class MainController {
	

	@Autowired UserService userService;
	
	
	@RequestMapping(value="/busroute.do", method = RequestMethod.GET)
	public String busRoute() {
		return "busr.jsp";
	}
	
	@RequestMapping(value="/main.do", method = RequestMethod.GET)
	public String mainPage() {
		return "main.jsp";
	}
	
	@RequestMapping(value="/chattingPage.do", method = RequestMethod.GET)
	public String chattingPage() {
		return "chat-ws.jsp";
	}
	
	@RequestMapping(value="/echoPage.do", method = RequestMethod.GET)
	public String echoPage() {
		return "echo-ws.jsp";
	}
	
	@RequestMapping(value="/traintime.do", method = RequestMethod.GET)
	public String trainPage() {
		return "traintime.jsp";
	}
	
	@RequestMapping(value="/bustime.do", method = RequestMethod.GET)
	public String bustimePage() {
		return "bustime.jsp";
	}
	
	@RequestMapping(value="/chatPage.do", method = RequestMethod.GET)
	public String chatPage() {
		return "chat.jsp";
	}
	
	@RequestMapping(value="/loginPage.do", method = RequestMethod.GET)
	public String loginPage() {
		return "login.jsp";
	}
	
	
	@RequestMapping(value="/login.do", method = RequestMethod.POST) public String
		login(HttpSession session, @ModelAttribute UserVO user) {
		if(userService.selectUsed(user.getUserId()).equals("Y") &&
			userService.selectPwd(user.getUserId(), user.getPwd())) {
			userService.setSession(session, user.getUserId());
		
			return "redirect:/tradeListPage.do"; 
		} else {
		 
			return "login.jsp"; 
		} 
	}
	
	
	@RequestMapping(value="/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.removeAttribute("USER");
		return "redirect:/tradeListPage.do";
	}
}
