package com.blog.project.app.models.service.implementation;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Notifications;
import com.blog.project.app.entities.Notifications.NotificationDetails;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.dao.INotifications;
import com.blog.project.app.models.dao.IUser;
import com.blog.project.app.models.service.INotificationService;
import com.blog.project.app.models.service.IUserService;

@Service
public class NotificationServiceImpl implements INotificationService {

	@Autowired
	private INotifications notificationDao;

	@Autowired
	private IUser userDao;

	@Autowired
	private IUserService userService;

	@Override
	public List<NotificationDetails> findAllNotifications() {
		return notificationDao.findAllProjectionBy();
	}

	@Override
	public List<NotificationDetails> findNotificationsOfUser(User user) {

		return changeAllNotificationsToRead(notificationDao.findAllNotificationsByNotificationOfOrderByCreatedAtDesc(user));
	}

	public List<NotificationDetails> changeAllNotificationsToRead(List<NotificationDetails> notificationDetails) {
		List<NotificationDetails> auxNotificationDetails = new LinkedList<>();

		for (NotificationDetails iteratingNotifications : notificationDetails) {

			auxNotificationDetails.add(iteratingNotifications);
			if (!iteratingNotifications.getIsRead()) {
				Optional<Notifications> notification = notificationDao.findById(iteratingNotifications.getId());
				notification.get().setRead(true);
				notificationDao.save(notification.get());
			}
		}

		return auxNotificationDetails;
	}

	@Transactional
	public void newNotification(String typeNotification, String toNotification, String authorNotification) {
		User to = userService.getUserByUsername(toNotification);
		User from = userService.getUserByUsername(authorNotification);
		System.out.println(from);

		this.newNotificationUserObject(typeNotification, to, from);

	}

	@Transactional
	public void newNotificationUserObject(String typeNotification, User to, User from) {
		Notifications notification = new Notifications(typeNotification, to, from);

		System.out.println(from);
		System.out.println(to);
		notificationDao.save(notification);
	}

	@Transactional
	public void newNotificationUserObject(String typeNotification, User to, User from, Post post) {
		Notifications notification = new Notifications(typeNotification, to, from, post);

		notificationDao.save(notification);
	}

	@Transactional
	public void newNotificationUserObject(String typeNotification, User to, User from, Comments comment) {
		Notifications notification = new Notifications(typeNotification, to, from, comment);

		notificationDao.save(notification);
	}

	@Override
	public void deleteNotification(User user, Post post, String notificationType) {
		
		Notifications notification = notificationDao.findByRelatedWithAndRelatedPostAndNotificationType(user, post, notificationType);
		notificationDao.delete(notification);
		
	}

}
