package my.user.dao.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.user.dao.UserKeywordDAO;
import my.user.vo.UserKeywordVO;

@Repository
public class UserKeywordDAOImpl extends EgovAbstractMapper implements UserKeywordDAO{

	@Override
	public List<UserKeywordVO> findTitleKeyword() {
		return selectList("UserKeyword.findTitleKeyword");
	}

	@Override
	public List<UserKeywordVO> findUserKeyword(String userId) {
		return selectList("UserKeyword.findUserKeyword", userId);
	}

	@Override
	public UserKeywordVO findDuplKeyword(UserKeywordVO userkeyword) {
		return selectOne("UserKeyword.findDuplKeyword", userkeyword);
	}

	@Override
	public int insertKeyword(UserKeywordVO userkeyword) {
		return insert("UserKeyword.insertKeyword", userkeyword);
	}

	@Override
	public int deleteKeyword(int keywordId) {
		return delete("UserKeyword.deleteKeyword", keywordId);
	}

}
