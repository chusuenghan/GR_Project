package my.notification.vo;

public class SubscriptionVO {
	
	private String endpoint;
	private String expirationTime;
    private Keys keys;

    public String getExpirationTime() {
		return expirationTime;
	}


	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}


	public String getEndpoint() {
		return endpoint;
	}


	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}


	public Keys getKeys() {
		return keys;
	}


	public void setKeys(Keys keys) {
		this.keys = keys;
	}
	

	@Override
	public String toString() {
		return "Subscription [endpoint=" + endpoint + ", keys=" + keys + "]";
	}


	public static class Keys {
        private String p256dh;
        private String auth;
        
		public String getP256dh() {
			return p256dh;
		}
		public void setP256dh(String p256dh) {
			this.p256dh = p256dh;
		}
		public String getAuth() {
			return auth;
		}
		public void setAuth(String auth) {
			this.auth = auth;
		}
		@Override
		public String toString() {
			return "Keys [p256dh=" + p256dh + ", auth=" + auth + "]";
		}

    }
}
