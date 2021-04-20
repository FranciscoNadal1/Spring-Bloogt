package com.blog.project.app.models.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
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
	
	@Override
	@Transactional
	public int newChat(String message, User to) {
		User fromUser = userService.getLoggedUser();
		
		
		Message firstMessage = new Message(message, fromUser);
		messageDao.save(firstMessage);

		Chat newChat = new Chat(firstMessage);		
		newChat.getMessages().add(firstMessage);
		
		fromUser.getChats().add(newChat);
		to.getChats().add(newChat);		

		newChat.getUsersInvolved().add(userService.getLoggedUser());
		newChat.getUsersInvolved().add(to);
		
		userService.save(userService.getLoggedUser());
		userService.save(to);
		
		chatDao.save(newChat);
		
		return newChat.getId();		
	}

	@Override
	public List<Chat> findAllChats() {
		return (List<Chat>) chatDao.findAll();

	}

	@Override
	public List<Chat> findAllChatsOfLoggedUser() {
		User user = userService.getLoggedUser();
		return chatDao.getAllChatsOfUser(user.getId());

	}

	@Override
	public Chat findChatById(int chatId) {
		return chatDao.findById(chatId);
	}

	@Override
	@Transactional
	public void newMessageToChat(String message, int chatId) {
		User fromUser = userService.getLoggedUser();

		Chat chat = chatDao.findById(chatId);
		
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
		}catch(Exception e) {
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
		for(Message message : chat.getMessages()) {
			this.changeMessageToRead(message);
		}		
	}
	@Override
	public void allMessagesToReadExceptAuthored(Chat chat, User user) {
		for(Message message : chat.getMessages()) {
			if(!message.getAuthor().equals(user))
				this.changeMessageToRead(message);
		}		
	}

	@Override
	public int unreadMessagesFromChat(Chat chat) {
		try {
			User fromUser = userService.getLoggedUser();		
			return chatDao.getUnreadOfChat(fromUser.getId(), chat.getId());
			}catch(Exception e) {
				return 0;
			}

	}

}
