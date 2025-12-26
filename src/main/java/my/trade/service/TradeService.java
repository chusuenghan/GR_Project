package my.trade.service;

import java.util.List;

import my.trade.vo.PageVO;
import my.trade.vo.TradeVO;

public interface TradeService {
	public int insertTrade(TradeVO trade);
	public List<TradeVO> selectTradeList(PageVO pagination);
	public TradeVO selectTrade(int tradeId);
	public int updateTrade(TradeVO trade);
	public int deleteTrade(int tradeId);
	public int countPosts();
	public int countPostsBySearchTerm(String searchTerm);
	public List<TradeVO> selectMyTrade(String writerId);
	public int updateTradeStatus(TradeVO trade);
	public List<TradeVO> selectByTradeIds(List<Integer> tradeId);
	public String selectWriterId(int tradeId);
}
