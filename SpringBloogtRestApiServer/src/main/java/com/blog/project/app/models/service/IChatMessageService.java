package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.entities.chat.Message;

public interface IChatMessageService {

	public int newChat(String message, User to);
	public List<Chat> findAllChats();
	public List<Chat> findAllChatsOfLoggedUser();
	public Chat findChatById(int chatId);
	
	public void newMessageToChat(String message, int chatId);
	public int getUnreadMessages();
	
	public void allMessagesToRead(Chat chat);
	public void changeMessageToRead(Message message);
	public void allMessagesToReadExceptAuthored(Chat chat, User user);
	
	public int unreadMessagesFromChat(Chat chat);
}
