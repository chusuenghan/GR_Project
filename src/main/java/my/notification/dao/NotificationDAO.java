package my.notification.dao;

import java.util.List;

import my.notification.vo.NotificationVO;


public interface NotificationDAO {
	public List<NotificationVO> findNotification(String userId);
	public List<NotificationVO> findNotiIsRead(String userId);
	public int updateNotiIsRead(String userId);
	public int insertNotification(NotificationVO notification);
	public int deleteNotification(int notiId);
}
