package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Category.CategoryName;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.User.OnlyUsername;

@Entity
public class Post implements Serializable, Comparable<Post> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String title;

	private String imagePost;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	@Lob
	@Column(length = 8096)
	@NotEmpty
	private String content;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Hashtag_post", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
	private List<Hashtag> hashtags;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private List<Comments> comments;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date created_at) {
		this.createdAt = created_at;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImagePost() {
		return imagePost;
	}

	public void setImagePost(String imagePost) {
		this.imagePost = imagePost;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public void sortCommentsByDate() {
		Collections.sort(comments);
	}

	public List<Comments> getComments() {
		Collections.sort(comments);
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Custom methods
///////////
///////////		Custom methods for the post class	

	public List<Comments> getCommentsSortDateAsc() {
		Collections.sort(comments);
		return comments;
	}

	public List<Comments> getCommentsSortDateDesc() {
		Collections.reverse(comments);
		return comments;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface showPosts {

		String getId();

		String getTitle();

		String getContent();

		String getCreatedAt();

		List<HashtagShow> getHashtags();

		OnlyUsername getCreatedBy();

		String getImagePost();

		@Value("#{target.getComments().size()}")
		int getCommentaryCount();

		CategoryName getCategory();
	}

	public interface PostDetails extends showPosts{
		
/*
		String getId();

		String getTitle();

		String getContent();

		String getCreatedAt();

		List<HashtagShow> getHashtags();

		OnlyUsername getCreatedBy();

		String getImagePost();

		@Value("#{target.getComments().size()}")
		int getCommentaryCount();

		CategoryName getCategory();
*/
	}

	// It will load PostDetails and then add the sorted comments
	public interface PostDetailsCommentsSortByDateAsc extends PostDetails {
		@Value("#{target.getCommentsSortDateAsc()}")
		List<ShowComments> getComments();
	}

	// It will load PostDetails and then add the sorted comments
	public interface PostDetailsCommentsSortByDateDesc extends PostDetails {
		@Value("#{target.getCommentsSortDateDesc()}")
		List<ShowComments> getComments();
	}

	public interface PostDetailsCommentsSortByThumbsUp extends PostDetails {
		// TODO Thumbs up are not implemented !
	}

	public interface PostByUser {

		String getId();

		String getTitle();

		String getContent();

		Date getCreatedAt();

		List<HashtagShow> getHashtags();

		@Value("#{target.getComments().size()}")
		int getCommentaryCount();

		// List<ShowComments> getComments();
		CategoryName getCategory();
	}

	public interface PostByHashtag {

		String getId();

		String getTitle();

		String getContent();

		String getImagePost();

		Date getCreatedAt();

		@Value("#{target.getComments().size()}")
		int getCommentaryCount();

		CategoryName getCategory();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////		Comparators
///////////////
///////////////		Compare objects

	@Override
	public int compareTo(Post u) {

		if (getCreatedAt() == null || u.getCreatedAt() == null) {
			return 0;
		}
		return getCreatedAt().compareTo(u.getCreatedAt());
	}

}
