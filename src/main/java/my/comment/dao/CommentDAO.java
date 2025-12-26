package my.comment.dao;

import java.util.List;

import my.comment.vo.CommentVO;

public interface CommentDAO {
	public int insertComment(CommentVO comment);
	public List<CommentVO> selectCommentList(int tradeId);
}
