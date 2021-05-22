package com.blog.project.app.entities.chat;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.chat.Message.MessageData;

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
	
	public int getUnreadMessages() {
		List<Message> listMessages = this.getMessages();
		int unreadMessages = 0;
		
		for(Message message : listMessages) {
			if(!message.isRead())
				unreadMessages++;
		}
		return unreadMessages;		
	}
	
	public int getUnreadMessagesOfUser(User user) {
		List<Message> listMessages = this.getMessages();
		int unreadMessages = 0;
		
		for(Message message : listMessages) {
			if(!message.isRead() && !message.getAuthor().equals(user))
				unreadMessages++;
		}
		return unreadMessages;		
	}
	
	public int getMessagesOfUser(User user) {
		List<Message> listMessages = this.getMessages();
		int unreadMessages = 0;
		
		for(Message message : listMessages) {
			if(message.getAuthor().equals(user))
				unreadMessages++;
		}
		return unreadMessages;		
	}	

	public Map<String,Integer> getMessagesEachUser() {
		Map<String,Integer> map = new HashMap<String, Integer>();
		List<Message> listMessages = this.getMessages();		
		List<User> listUsersMessages = new LinkedList<User>();
		
		for(Message message : listMessages) {
			if(!listUsersMessages.contains(message.getAuthor()))
				listUsersMessages.add(message.getAuthor());
		}	
		
		
		for(User user : listUsersMessages) {
			map.put(user.getUsername(), getMessagesOfUser(user));
		}
		
		return map;
	}
	public Map<String,Integer> getUnreadMessagesEachUser() {
		Map<String,Integer> map = new HashMap<String, Integer>();
		List<Message> listMessages = this.getMessages();		
		List<User> listUsersMessages = new LinkedList<User>();
		
		for(User user : this.getUsersInvolved()) {
			if(!listUsersMessages.contains(user))
				listUsersMessages.add(user);
		}	
		/*
		for(Message message : listMessages) {
			if(!listUsersMessages.contains(message.getAuthor()))
				listUsersMessages.add(message.getAuthor());
		}	
		*/	
		
		for(User user : listUsersMessages) {
			map.put(user.getUsername(), getUnreadMessagesOfUser(user));
		}
		
		return map;
	}
	
	public Date getLastMessageDate() {
		List<Date> dateMessages = new LinkedList<Date>();
		List<Message> listMessages = this.getMessages();		
		
		for(Message message : listMessages) {			
			dateMessages.add(message.getCreatedAt());
		}	
		Collections.sort(dateMessages);
		Collections.reverse(dateMessages);
		
		if(dateMessages.isEmpty())
			return null;
		return dateMessages.get(0);
	}
	

	public Message getLastMessage() {
		List<Message> messages = this.getMessages();
		Collections.sort(messages);
		if(messages == null)
			return null;
		
		return messages.get(0);
	}
	public String toString() {
		return "Id of chat : " + this.getId();
	}
	

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields
	
	public interface ListChatsOfUser {
		int getId();
		List<OnlyUsername> getUsersInvolved();
		Date getCreatedAt();

		@Value("#{target.getMessages().size()}")
		int getMessageCount();

		@Value("#{target.getLastMessage()}")
		MessageData getLastMessage();

		int getUnreadMessages();
		Map<String,Integer> getUnreadMessagesEachUser();
		Map<String,Integer> getMessagesEachUser();
		Date getLastMessageDate();
		
	}
	public interface ChatsAndMessageOfUser extends ListChatsOfUser{
		List<MessageData> getMessages();
	}
}
