package com.blog.project.app.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Category.CategoryName;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.OnlyUsername;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "post_id", "user_id" }) })
public class SharedPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(value = AccessLevel.PRIVATE)
	private Long id;

	@ManyToOne()
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Post post;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User sharedBy;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields


	
	public interface SharedPostProjection {
		showPosts getPost();
		Date getCreatedAt();
		OnlyUsername getSharedBy();
	}
}
