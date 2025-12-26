package my.comment.vo;

import java.util.ArrayList;
import java.util.List;

public class CommentVO
{
  private int tradeId;
  private int commentId;
  private String writerId;
  private int parentId;
  private String content;
  private String nowDate;
  private List<CommentVO> children = new ArrayList();
	public int getTradeId() {
		return tradeId;
	}
	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getWriterId() {
		return writerId;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNowDate() {
		return nowDate;
	}
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	public List<CommentVO> getChildren() {
		return children;
	}
	public void setChildren(List<CommentVO> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "CommentVO [tradeId=" + tradeId + ", commentId=" + commentId + ", writerId=" + writerId + ", parentId="
				+ parentId + ", content=" + content + ", nowDate=" + nowDate + ", children=" + children + "]";
	}
}