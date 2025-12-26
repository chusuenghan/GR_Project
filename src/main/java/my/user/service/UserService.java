package my.user.service;

import javax.servlet.http.HttpSession;

import my.user.vo.UserVO;

public interface UserService {
	public Boolean selectPwd(String id, String pwd);
	
	public int insertUser(UserVO user);
	
	public UserVO selectUserInfo(String userId);
	
	public Boolean setSession(HttpSession session, String userId);
	
	public int updateUser(UserVO user);
	
	public int deleteUser(String userId);
	
	public String selectUsed(String userId);
}
