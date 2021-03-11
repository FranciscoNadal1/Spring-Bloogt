package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Category.CategoryName;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.User.OnlyUsername;

@Entity
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String title;

	@NotEmpty
	private String createdAt;

	@NotEmpty
	private String content;

	@ManyToMany
	@JoinTable(name = "Hashtag_post", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
	private List<Hashtag> hashtags;

	@OneToMany
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String created_at) {
		this.createdAt = created_at;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public List<Comments> getComments() {
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
		
		@Value("#{target.getComments().size()}")
	    int getCommentaryCount();
		CategoryName getCategory();
	}
	public interface PostDetails {

		String getId();
		
		String getTitle();
		String getContent();
		String getCreatedAt();
		List<HashtagShow> getHashtags();
		OnlyUsername getCreatedBy();
		
		
		@Value("#{target.getComments().size()}")
	    int getCommentaryCount();

		
		List<ShowComments> getComments();
		CategoryName getCategory();
	}	
	
	public interface PostByUser {

		String getId();
		
		String getTitle();
		String getContent();
		String getCreatedAt();
		List<Hashtag> getHashtags();
		List<ShowComments> getComments();
		CategoryName getCategory();
	}
	public interface PostByHashtag {

		String getId();
		
		String getTitle();
		String getContent();
		String getCreatedAt();
		@Value("#{target.getComments().size()}")
	    int getCommentaryCount();
		CategoryName getCategory();
	}
}
