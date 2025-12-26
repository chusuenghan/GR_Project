package my.notification.dao.impl;

import java.util.List;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import my.notification.dao.NotificationDAO;
import my.notification.vo.NotificationVO;

@Repository
public class NotificationDAOImpl extends EgovAbstractMapper implements NotificationDAO{

	@Override
	public List<NotificationVO> findNotification(String userId) {
		return selectList("Notification.findNotification", userId);
	}

	@Override
	public List<NotificationVO> findNotiIsRead(String userId) {
		return selectList("Notification.findNotiIsRead", userId);
	}

	@Override
	public int updateNotiIsRead(String userId) {
		return update("Notification.updateNotiIsRead", userId);
	}

	@Override
	public int insertNotification(NotificationVO notification) {
		return insert("Notification.insertNotification", notification);
	}

	@Override
	public int deleteNotification(int notiId) {
		return delete("Notification.deleteNotification", notiId);
	}

}
