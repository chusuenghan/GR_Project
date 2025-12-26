package my.notification.dao;

import java.util.List;

import my.notification.vo.NotiSubscriptionVO;

public interface NotiSubscriptionDAO {
	public List<NotiSubscriptionVO> findUserNotiSub(String userId);
	public int insertNotiSub(NotiSubscriptionVO notisub);
}
