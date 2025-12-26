package my.user.service;

import my.user.vo.UserRatingVO;

public interface UserRatingService {
	public UserRatingVO avgRating(String userId);
	public int insertRating(UserRatingVO userrating);
}
