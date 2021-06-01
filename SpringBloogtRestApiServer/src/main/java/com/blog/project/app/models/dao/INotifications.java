package com.blog.project.app.models.dao;

import java.util.List;
import java.util.Optional;

import com.blog.project.app.entities.Notifications;
import com.blog.project.app.entities.Notifications.NotificationDetails;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User;

public interface INotifications extends BaseRepository <Notifications, String> {

	int countByNotificationOfAndIsReadFalse(User user);
	Optional<Notifications> findById(String id);

	Notifications findByRelatedWithAndRelatedPostAndNotificationType(User user, Post post, String notificationType);
	//List<NotificationDetails> findAll();
	List<NotificationDetails> findAllProjectionBy();
	List<NotificationDetails> findAllNotificationsByNotificationOfOrderByCreatedAtDesc(User user);

}