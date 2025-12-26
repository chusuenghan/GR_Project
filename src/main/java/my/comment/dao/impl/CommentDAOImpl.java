package my.comment.dao.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.comment.dao.CommentDAO;
import my.comment.vo.CommentVO;

@Repository
public class CommentDAOImpl extends EgovAbstractMapper implements CommentDAO{
	
	@Override
	public int insertComment(CommentVO comment) {
		return insert("Comment.insertComment", comment);
	}
	
	@Override
	public List<CommentVO> selectCommentList(int tradeId){
		return selectList("Comment.selectCommentList", tradeId);
	}
}
