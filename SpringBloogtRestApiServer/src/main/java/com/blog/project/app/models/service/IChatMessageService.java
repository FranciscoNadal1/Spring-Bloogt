package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.entities.chat.Chat.ChatsAndMessageOfUser;
import com.blog.project.app.entities.chat.Chat.ListChatsOfUser;
import com.blog.project.app.entities.chat.Message;

public interface IChatMessageService {

	public int newChatFromTo(User from, String message, User to);
	
	public List<Chat> findAllChats();
	public List<Chat> findAllChatsOfLoggedUser();
	public List<ListChatsOfUser> findAllChatsOfLoggedUserProjection(User user);
	public ChatsAndMessageOfUser findChatOfUserProjection(Chat chat, User user);
	public Chat findChatById(int chatId);
	public void newMessageToChat(User fromUser, String message, int chatId);
	
	public int getUnreadMessagesLoggedSession();
	public int getUnreadMessages(User user);
	
	public void allMessagesToRead(Chat chat);
	public void changeMessageToRead(Message message);
	public void allMessagesToReadExceptAuthored(Chat chat, User user);
	
	public int unreadMessagesFromChat(Chat chat);
	
	public void deleteChat(Chat chat);
}
