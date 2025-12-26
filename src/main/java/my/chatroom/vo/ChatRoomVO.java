package my.chatroom.vo;

public class ChatRoomVO
{
  private String roomId;
  private String sender;
  private String chatContent;
  private String chatTime;
public String getRoomId() {
	return roomId;
}
public void setRoomId(String roomId) {
	this.roomId = roomId;
}
public String getSender() {
	return sender;
}
public void setSender(String sender) {
	this.sender = sender;
}
public String getChatContent() {
	return chatContent;
}
public void setChatContent(String chatContent) {
	this.chatContent = chatContent;
}
public String getChatTime() {
	return chatTime;
}
public void setChatTime(String chatTime) {
	this.chatTime = chatTime;
}
@Override
public String toString() {
	return "ChatRoomVO [roomId=" + roomId + ", sender=" + sender + ", chatContent=" + chatContent + ", chatTime="
			+ chatTime + "]";
}
  
  
}