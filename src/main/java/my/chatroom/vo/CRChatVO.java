package my.chatroom.vo;

public class CRChatVO
{
  private String roomId;
  private String user1Id;
  private String user2Id;
  private String otherUserId;
  private int tradeId;
  
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getUser1Id() {
		return user1Id;
	}
	public void setUser1Id(String user1Id) {
		this.user1Id = user1Id;
	}
	public String getUser2Id() {
		return user2Id;
	}
	public void setUser2Id(String user2Id) {
		this.user2Id = user2Id;
	}
	public String getOtherUserId() {
		return otherUserId;
	}
	public void setOtherUserId(String otherUserId) {
		this.otherUserId = otherUserId;
	}
	public int getTradeId() {
		return tradeId;
	}
	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}
	@Override
	public String toString() {
		return "CRChatVO [roomId=" + roomId + ", user1Id=" + user1Id + ", user2Id=" + user2Id + ", otherUserId="
				+ otherUserId + ", tradeId=" + tradeId + "]";
	}
}