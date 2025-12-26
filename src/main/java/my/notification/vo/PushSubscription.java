package my.notification.vo;

public class PushSubscription {
	
	private String userId;
    private SubscriptionVO subscription;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public SubscriptionVO getSubscription() {
		return subscription;
	}
	public void setSubscription(SubscriptionVO subscription) {
		this.subscription = subscription;
	}
	@Override
	public String toString() {
		return "PushSubscription [userId=" + userId + ", subscription=" + subscription + "]";
	}
}
