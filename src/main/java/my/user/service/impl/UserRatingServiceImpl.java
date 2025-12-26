package my.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.user.dao.UserRatingDAO;
import my.user.service.UserRatingService;
import my.user.vo.UserRatingVO;

@Service
public class UserRatingServiceImpl implements UserRatingService{
	
	@Autowired(required=false)
	UserRatingDAO userratingDAO;
	
	@Override
	public UserRatingVO avgRating(String userId) {
		return userratingDAO.avgRating(userId);
	}

	@Override
	public int insertRating(UserRatingVO userrating) {
		return userratingDAO.insertRating(userrating);
	}

}
