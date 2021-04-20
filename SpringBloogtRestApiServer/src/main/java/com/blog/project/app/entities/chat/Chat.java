package com.blog.project.app.entities.chat;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.User;

@Entity
public class Chat implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany
	@JoinColumn(name = "chat_id", referencedColumnName = "id")
	private List<Message> messages;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;
	
	@ManyToMany(mappedBy = "chats")
	private List<User> usersInvolved;

	public Chat() {
		
	}
	public Chat(Message message) {
		this.usersInvolved = new LinkedList<User>();
		this.messages = new LinkedList<Message>();	

		this.createdAt = new Date();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<User> getUsersInvolved() {
		return usersInvolved;
	}

	public void setUsersInvolved(List<User> usersInvolved) {
		this.usersInvolved = usersInvolved;
	}

	public int getId() {
		return id;
	}


	public String toString() {
		return "Id of chat : " + this.getId();
	}
	

}
