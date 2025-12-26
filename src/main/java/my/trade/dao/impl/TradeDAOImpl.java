package my.trade.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import my.trade.dao.TradeDAO;
import my.trade.vo.PageVO;
import my.trade.vo.TradeVO;

@Repository
public class TradeDAOImpl extends EgovAbstractMapper implements TradeDAO{
	
	@Override
	public int insertTrade(TradeVO trade) {
		return insert("Trade.insertTrade", trade);
	}

	@Override
	public List<TradeVO> selectTradeList(PageVO pagination){
		return selectList("Trade.selectTradeList", pagination);
	}

	@Override
	public TradeVO selectTrade(int tradeId) {
		return selectOne("Trade.selectTrade", tradeId);
	}
	
	@Override
	public int updateTrade(TradeVO trade){
		return update("Trade.updateTrade", trade);
	}

	@Override
	public int deleteTrade(int tradeId){
		return update("Trade.deleteTrade", tradeId);
	}
	
	@Override
	public int countPosts(){
		return selectOne("Trade.countPosts");
	}
	
	@Override
	public int countPostsBySearchTerm(String searchTerm){
		return selectOne("Trade.countPostsBySearchTerm", searchTerm);
	}

	@Override
	public List<TradeVO> selectMyTrade(String writerId) {
		return selectList("Trade.selectMyTrade", writerId);
	}

	@Override
	public int updateTradeStatus(TradeVO trade) {
		return update("Trade.updateTradeStatus", trade);
	}

	@Override
	public List<TradeVO> selectByTradeIds(List<Integer> tradeId) {
		return selectList("Trade.selectByTradeIds", tradeId);
	}

	@Override
	public String selectWriterId(int tradeId) {
		return selectOne("Trade.selectWriterId", tradeId);
	}
}
