package my.notification.vo;

public class NotificationVO {
	private int notiId;
	private String userId;
	private int tradeId;
	private String isRead;
	private String makeDate;
	private int userCount;
	private String notiStatus;
	
	public String getNotiStatus() {
		return notiStatus;
	}
	public void setNotiStatus(String notiStatus) {
		this.notiStatus = notiStatus;
	}
	public int getNotiId() {
		return notiId;
	}
	public void setNotiId(int notiId) {
		this.notiId = notiId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getTradeId() {
		return tradeId;
	}
	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getMakeDate() {
		return makeDate;
	}
	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	@Override
	public String toString() {
		return "NotificationVO [notiId=" + notiId + ", userId=" + userId + ", tradeId=" + tradeId + ", isRead=" + isRead
				+ ", makeDate=" + makeDate + ", userCount=" + userCount + ", notiStatus=" + notiStatus + "]";
	}

}
