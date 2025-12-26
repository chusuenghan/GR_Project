package my.comment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.comment.dao.CommentDAO;
import my.comment.service.CommentService;
import my.comment.vo.CommentVO;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	CommentDAO commentDAO;
	
	@Override
	public int insertComment(CommentVO comment) {
		System.out.println(comment.toString());
		return commentDAO.insertComment(comment);
	}
	
	@Override
	public List<CommentVO> selectCommentList(int tradeId){
		return commentDAO.selectCommentList(tradeId);
		
//		List<CommentVO> comments = commentDAO.selectCommentList(tradeId);
//		
//		Map<Integer, CommentVO> commentMap = new HashMap<>();
//        for (CommentVO comment : comments) {
//            commentMap.put(comment.getCommentId(), comment);
//        }
//
//        List<CommentVO> topLevelComments = new ArrayList<>();
//
//        for (CommentVO comment : comments) {
//            if (comment.getParentId() == 0) {
//                topLevelComments.add(comment);
//            } else {
//                CommentVO parent = commentMap.get(comment.getParentId());
//                if (parent != null) {
//                    parent.getChildren().add(comment);
//                }
//            }
//        }
//        
//        return topLevelComments;
	}
}
