package my.notification.service;

import java.util.List;

import my.notification.vo.NotiSubscriptionVO;

public interface NotiSubscriptionService {
	public List<NotiSubscriptionVO> findUserNotiSub(String userId);
	public int insertNotiSub(NotiSubscriptionVO notisub);
}
