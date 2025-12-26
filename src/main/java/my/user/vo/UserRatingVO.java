package my.user.vo;

public class UserRatingVO {
	private String userId;
	private float rating;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	@Override
	public String toString() {
		return "UserRatingVO [userId=" + userId + ", rating=" + rating + "]";
	}
}
