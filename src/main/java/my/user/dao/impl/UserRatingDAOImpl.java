package my.user.dao.impl;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.user.dao.UserRatingDAO;
import my.user.vo.UserRatingVO;

@Repository
public class UserRatingDAOImpl extends EgovAbstractMapper implements UserRatingDAO {

	@Override
	public UserRatingVO avgRating(String userId) {
		return selectOne("UserRating.avgRating", userId);
	}

	@Override
	public int insertRating(UserRatingVO userrating) {
		return insert("UserRating.insertRating", userrating);
	}

}
