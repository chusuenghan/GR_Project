package my.user.dao;

import my.user.vo.UserRatingVO;

public interface UserRatingDAO {
	public UserRatingVO avgRating(String userId);
	public int insertRating(UserRatingVO userrating);
}
