package my.user.vo;

public class UserKeywordVO {
	private String userId;
	private String keyword;
	private int keywordId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(int keywordId) {
		this.keywordId = keywordId;
	}
	@Override
	public String toString() {
		return "UserKeywordVO [userId=" + userId + ", keyword=" + keyword + ", keywordId=" + keywordId + "]";
	}

}
