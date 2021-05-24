package com.blog.project.app.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.OnlyUsername;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notifications {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	private String notificationType;

	boolean isRead;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User notificationOf;

	@ManyToOne
	@JoinColumn(name = "notificationAuthor_id", referencedColumnName = "id")
	private User relatedWith;

	@ManyToOne()
	@JoinColumn(name = "relatedPost_id", referencedColumnName = "id")
	private Post relatedPost;

	@ManyToOne()
	@JoinColumn(name = "relatedComment_id", referencedColumnName = "id")
	private Comments relatedComment;

	public Notifications(String notificationType, User notificationOf, User relatedWith, Post post) {
		this(notificationType, notificationOf, relatedWith);
		this.relatedPost = post;
	}

	public Notifications(String notificationType, User notificationOf, User relatedWith, Comments comment) {
		this(notificationType, notificationOf, relatedWith);
		this.relatedComment = comment;
	}

	public Notifications(String notificationType, User notificationOf, User relatedWith) {

		System.out.println(relatedWith);
		System.out.println(notificationOf);
		this.notificationType = notificationType;
		this.isRead = false;
		this.notificationOf = notificationOf;
		this.relatedWith = relatedWith;

		System.out.println(this.relatedWith);
		System.out.println(this.notificationOf);
		this.createdAt = new Date();

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface NotificationDetails {
		
		String getId();

		String getNotificationType();

		boolean getIsRead();

		Date getCreatedAt();

		OnlyUsername getNotificationOf();

		OnlyUsername getRelatedWith();

		showPosts getRelatedPost();

		ShowComments getRelatedComment();

	}
}
