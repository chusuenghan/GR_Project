package my.comment.service;

import java.util.List;

import my.comment.vo.CommentVO;

public interface CommentService {
	public int insertComment(CommentVO comment);
	public List<CommentVO> selectCommentList(int tradeId);
}
