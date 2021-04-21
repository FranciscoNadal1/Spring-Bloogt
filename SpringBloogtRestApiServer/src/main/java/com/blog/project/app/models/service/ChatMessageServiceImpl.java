package com.blog.project.app.models.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.entities.chat.Chat.ChatsAndMessageOfUser;
import com.blog.project.app.entities.chat.Chat.ListChatsOfUser;
import com.blog.project.app.entities.chat.Message;
import com.blog.project.app.models.dao.IChat;
import com.blog.project.app.models.dao.IMessage;

@Service
public class ChatMessageServiceImpl implements IChatMessageService {

	@Autowired
	IChat chatDao;

	@Autowired
	IMessage messageDao;

	@Autowired
	IUserService userService;

	private static final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

	@Override
	@Transactional
	public int newChatFromTo(User fromUser, String message, User to) {

		Message firstMessage = new Message(message, fromUser);
		messageDao.save(firstMessage);

		Chat newChat = new Chat(firstMessage);
		newChat.getMessages().add(firstMessage);

		fromUser.getChats().add(newChat);
		to.getChats().add(newChat);

		newChat.getUsersInvolved().add(fromUser);
		newChat.getUsersInvolved().add(to);
/*
		userService.save(fromUser);
		userService.save(to);
*/
		chatDao.save(newChat);

		logger.info("Created chat " + fromUser + "-" + to);
		return newChat.getId();
	}



	@Override
	public List<Chat> findAllChats() {
		return (List<Chat>) chatDao.findAll();

	}

	@Override
	public List<Chat> findAllChatsOfLoggedUser() {
		User user = userService.getLoggedUser();
		return chatDao.findAllChatsByUsersInvolvedContains(user);
	}

	@Override
	public List<ListChatsOfUser> findAllChatsOfLoggedUserProjection(User user) {
		return chatDao.findProjectionAllChatsByUsersInvolvedContains(user);
	}

	@Override
	public ChatsAndMessageOfUser findChatOfUserProjection(Chat chat, User user) {
		return chatDao.findByIdAndUsersInvolvedContains(chat.getId(), user);
	}

	@Override
	public Chat findChatById(int chatId) {
		return chatDao.findById(chatId);
	}

	@Override
	@Transactional
	public void newMessageToChat(User fromUser, String message, int chatId) {
	//	User fromUser = userService.getLoggedUser();

		Chat chat = chatDao.findById(chatId);
		if(!chat.getUsersInvolved().contains(fromUser))
			throw new RuntimeException("You can't send a message on this chat");

		Message newMessage = new Message(message, fromUser);
		chat.getMessages().add(newMessage);

		messageDao.save(newMessage);
		chatDao.save(chat);

	}

	@Override
	public int getUnreadMessages() {
		try {
			User fromUser = userService.getLoggedUser();
			return chatDao.getUnreadMessages(fromUser.getId());
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void changeMessageToRead(Message message) {
		message.setRead(true);
		messageDao.save(message);
	}

	@Override
	public void allMessagesToRead(Chat chat) {
		for (Message message : chat.getMessages()) {
			this.changeMessageToRead(message);
		}
	}

	@Override
	public void allMessagesToReadExceptAuthored(Chat chat, User user) {
		for (Message message : chat.getMessages()) {
			if (!message.getAuthor().equals(user))
				this.changeMessageToRead(message);
		}
	}

	@Override
	public int unreadMessagesFromChat(Chat chat) {
		try {
			User fromUser = userService.getLoggedUser();
			return chatDao.getUnreadOfChat(fromUser.getId(), chat.getId());
		} catch (Exception e) {
			return 0;
		}

	}



	@Override
	public void deleteChat(Chat chat) {
		
		//	Set method doesn't follow an order, so we can iterate it while deleting
		Set<User> listUsersInvolved = new HashSet<>(chat.getUsersInvolved());
		Set<Message> listOfMessages = new HashSet<>(chat.getMessages());
		
		for(Message message : listOfMessages) {
			chat.getMessages().remove(message);
			messageDao.save(message);
			messageDao.delete(message);
		}

		for(User user : listUsersInvolved) {
			System.out.println("fucking "+user+"is involved");
			chat.getUsersInvolved().remove(user);
			user.getChats().remove(chat);			
			userService.save(user);
			
		}

		chatDao.delete(chat);	
		
		
	}

}
