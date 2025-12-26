package my.trade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.trade.dao.TradeDAO;
import my.trade.service.TradeService;
import my.trade.vo.PageVO;
import my.trade.vo.TradeVO;




@Service
public class TradeServiceImpl implements TradeService{

	@Autowired(required=false)
	TradeDAO tradeDAO;
	
	@Override
	public int insertTrade(TradeVO trade) {
		return tradeDAO.insertTrade(trade);
	}

	@Override
	public List<TradeVO> selectTradeList(PageVO pagination) {
		return tradeDAO.selectTradeList(pagination);
	}

	@Override
	public TradeVO selectTrade(int tradeId) {
		return tradeDAO.selectTrade(tradeId);
	}
	
	@Override
	public int updateTrade(TradeVO trade){
		return tradeDAO.updateTrade(trade);
	}

	@Override
	public int deleteTrade(int tradeId){
		return tradeDAO.deleteTrade(tradeId);
	}
	
	@Override
	public int countPosts(){
		return tradeDAO.countPosts();
	}
	
	@Override
	public int countPostsBySearchTerm(String searchTerm){
		return tradeDAO.countPostsBySearchTerm(searchTerm);
	}

	@Override
	public List<TradeVO> selectMyTrade(String writerId) {
		return tradeDAO.selectMyTrade(writerId);
	}

	@Override
	public int updateTradeStatus(TradeVO trade) {
		return tradeDAO.updateTradeStatus(trade);
	}

	@Override
	public List<TradeVO> selectByTradeIds(List<Integer> tradeId) {
		return tradeDAO.selectByTradeIds(tradeId);
	}

	@Override
	public String selectWriterId(int tradeId) {
		return tradeDAO.selectWriterId(tradeId);
	}
}
