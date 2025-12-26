package my.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.user.dao.UserKeywordDAO;
import my.user.service.UserKeywordService;
import my.user.vo.UserKeywordVO;

@Service
public class UserKeywordServiceImpl implements UserKeywordService{
	
	@Autowired
	UserKeywordDAO userkeywordDAO;
	
	@Override
	public List<UserKeywordVO> findTitleKeyword() {
		return userkeywordDAO.findTitleKeyword();
	}

	@Override
	public List<UserKeywordVO> findUserKeyword(String userId) {
		return userkeywordDAO.findUserKeyword(userId);
	}

	@Override
	public UserKeywordVO findDuplKeyword(UserKeywordVO userkeyword) {
		return userkeywordDAO.findDuplKeyword(userkeyword);
	}

	@Override
	public int insertKeyword(UserKeywordVO userkeyword) {
		return userkeywordDAO.insertKeyword(userkeyword);
	}

	@Override
	public int deleteKeyword(int keywordId) {
		return userkeywordDAO.deleteKeyword(keywordId);
	}

}
