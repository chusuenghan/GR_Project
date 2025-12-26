package my.notification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.notification.dao.NotiSubscriptionDAO;
import my.notification.service.NotiSubscriptionService;
import my.notification.vo.NotiSubscriptionVO;

@Service
public class NotiSubscriptionServiceImpl implements NotiSubscriptionService{
	
	@Autowired
	NotiSubscriptionDAO notisubscriptionDAO;

	@Override
	public List<NotiSubscriptionVO> findUserNotiSub(String userId) {
		return notisubscriptionDAO.findUserNotiSub(userId);
	}

	@Override
	public int insertNotiSub(NotiSubscriptionVO notisub) {
		return notisubscriptionDAO.insertNotiSub(notisub);
	}
}
