package my.user.dao;

import java.util.List;

import my.user.vo.UserKeywordVO;

public interface UserKeywordDAO {
	public List<UserKeywordVO> findTitleKeyword();
	public List<UserKeywordVO> findUserKeyword(String userId);
	public UserKeywordVO findDuplKeyword(UserKeywordVO userkeyword);
	public int insertKeyword(UserKeywordVO userkeyword);
	public int deleteKeyword(int keywordId);
}
