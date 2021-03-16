package com.blog.project.app.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Post.PostByHashtag;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.utils.LocalUtils;

@Entity
public class Comments  implements Comparable<Comments> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String message;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Post post;

//	@Temporal(TemporalType.DATE)
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Comments() {
	}
	
	public Comments(String message, User createdBy, Post post) {
		this.message = message;
		this.createdBy = createdBy;
		this.post = post;
		this.createdAt = LocalUtils.getActualDate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields
	
	
	public interface ShowComments extends Comparable<ShowComments> {

		String getId();
		String getMessage();
		OnlyUsername getCreatedBy();
		Date getCreatedAt();
		
		void setPost(Post post);
		
	    @Override public default int compareTo(ShowComments u) { 
	        if (getCreatedAt() == null || u.getCreatedAt() == null) { 
	          return 0; 
	        } 
	        return getCreatedAt().compareTo(u.getCreatedAt());
	      } 
	}

	public interface ShowAllComments {

		String getId();
		OnlyUsername getCreatedBy();
		String getMessage();
		PostByHashtag getPost();
		//OnlyUsername getCreatedBy();
		Date getCreatedAt();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////		Comparators
///////////////
///////////////		Compare objects

    @Override public int compareTo(Comments u) { 

        if (getCreatedAt() == null || u.getCreatedAt() == null) { 
          return 0; 
        } 
        return getCreatedAt().compareTo(u.getCreatedAt());
      } 
    /*
    public int compareTo(Comments c) {
        return getCreatedAt().compareTo(c.getCreatedAt());    	
    }*/

}
