package com.blog.project.app.entities.chat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.OnlyUsername;

@Entity
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String message;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;
	
	@OneToOne
	private User author;

	private boolean isRead;
	
	public Message() {
		
	}
	
	public Message(String message, User author) {

		this.createdAt = new Date();
		this.message = message;
		this.author = author;
		
		this.setRead(false);
	}

////////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields
	

	public interface MessageData {
		int getId();
		String message();
		OnlyUsername getAuthor();
		Date getCreatedAt();
		boolean isRead();
	}	
}
