package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Notifications;
import com.blog.project.app.entities.Notifications.NotificationDetails;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;

public interface INotificationService {

	
	public List<NotificationDetails> findAllNotifications();	
	public List<NotificationDetails> findNotificationsOfUser(User user);
	
	
	public void newNotification(String typeNotification, String toNotification, String authorNotification) ;
	public void newNotificationUserObject(String typeNotification, User toNotification, User authorNotification);
	public void newNotificationUserObject(String typeNotification, User to, User from, Post post);
	public void newNotificationUserObject(String typeNotification, User to, User from, Comments comment);
	
	public void deleteNotification(User user, Post post, String notificationType);

}
