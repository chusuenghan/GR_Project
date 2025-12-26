package my.notification.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.notification.dao.NotificationDAO;
import my.notification.service.NotificationService;
import my.notification.vo.NotificationVO;

@Service
public class NotificationServiceImpl implements NotificationService{
	
	@Autowired
	NotificationDAO notificationDAO;
	
	@Override
	public List<NotificationVO> findNotification(String userId) {
		return notificationDAO.findNotification(userId);
	}

	@Override
	public List<NotificationVO> findNotiIsRead(String userId) {
		return notificationDAO.findNotiIsRead(userId);
	}

	@Override
	public int updateNotiIsRead(String userId) {
		return notificationDAO.updateNotiIsRead(userId);
	}

	@Override
	public int insertNotification(NotificationVO notification) {
		return notificationDAO.insertNotification(notification);
	}

	@Override
	public int deleteNotification(int notiId) {
		return notificationDAO.deleteNotification(notiId);
	}

}
