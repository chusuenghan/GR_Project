package my.user.dao.impl;

import org.springframework.stereotype.Repository;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import my.user.dao.UserDAO;
import my.user.vo.UserVO;


@Repository
public class UserDAOImpl extends EgovAbstractMapper implements UserDAO {
	@Override
	public String selectPwd(String userId) {
		return selectOne("User.selectPwd", userId);
	}

	@Override
	public int insertUser(UserVO user) {
		return insert("User.insertUser", user);
	}

	@Override
	public UserVO selectUserInfo(String userId) {
		return selectOne("User.selectUserInfo",userId);
	}

	@Override
	public int updateUser(UserVO user) {
		return update("User.updateUser", user);
	}

	@Override
	public int deleteUser(String userId) {
		return delete("User.deleteUser", userId);
	}

	@Override
	public String selectUsed(String userId) {
		return selectOne("User.selectUsed", userId);
	}
}
