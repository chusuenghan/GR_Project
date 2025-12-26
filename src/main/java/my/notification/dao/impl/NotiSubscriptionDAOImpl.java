package my.notification.dao.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.notification.dao.NotiSubscriptionDAO;
import my.notification.vo.NotiSubscriptionVO;

@Repository
public class NotiSubscriptionDAOImpl extends EgovAbstractMapper implements NotiSubscriptionDAO{

	@Override
	public List<NotiSubscriptionVO> findUserNotiSub(String userId) {
		return selectList("NotiSubscription.findUserNotiSub", userId);
	}

	@Override
	public int insertNotiSub(NotiSubscriptionVO notisub) {
		return insert("NotiSubscription.insertNotiSub", notisub);
	}

}
