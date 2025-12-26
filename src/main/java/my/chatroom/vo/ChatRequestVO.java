package my.chatroom.vo;

public class ChatRequestVO
{
  private String requesterId;
  private String writerId;
  private int tradeId;
  private String reqStatus;
  private int requestId;
public String getRequesterId() {
	return requesterId;
}
public void setRequesterId(String requesterId) {
	this.requesterId = requesterId;
}
public String getWriterId() {
	return writerId;
}
public void setWriterId(String writerId) {
	this.writerId = writerId;
}
public int getTradeId() {
	return tradeId;
}
public void setTradeId(int tradeId) {
	this.tradeId = tradeId;
}
public String getReqStatus() {
	return reqStatus;
}
public void setReqStatus(String reqStatus) {
	this.reqStatus = reqStatus;
}
public int getRequestId() {
	return requestId;
}
public void setRequestId(int requestId) {
	this.requestId = requestId;
}
@Override
public String toString() {
	return "ChatRequestVO [requesterId=" + requesterId + ", writerId=" + writerId + ", tradeId=" + tradeId
			+ ", reqStatus=" + reqStatus + ", requestId=" + requestId + "]";
}
  
}