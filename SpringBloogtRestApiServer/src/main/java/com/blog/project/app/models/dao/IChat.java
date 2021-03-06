package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.entities.chat.Chat.ChatsAndMessageOfUser;
import com.blog.project.app.entities.chat.Chat.ListChatsOfUser;

public interface IChat extends BaseRepository <Chat, Long> {
	
	List<Chat> findAllChatsByUsersInvolvedContains(User user);
	List<ListChatsOfUser> findProjectionAllChatsByUsersInvolvedContains(User user);
	ChatsAndMessageOfUser findByIdAndUsersInvolvedContains(int chatId, User user);
	
	@Query(value = "SELECT count(*)\r\n" + 
			"	FROM chat, message, user_chats\r\n" + 
			"	WHERE chat.id = message.chat_id\r\n" + 
			"	and user_chats.chats_id = chat.id\r\n" + 
			"	and message.is_read = 0\r\n" + 
			"	and user_chats.users_involved_id = ?1\r\n" + 
			"	and message.author_id <> ?1", nativeQuery = true)		
	int getUnreadMessages(int userid);
	
	@Query(value = "SELECT count(*)\r\n" + 
			"	FROM chat, message, user_chats\r\n" + 
			"	WHERE chat.id = message.chat_id\r\n" + 
			"	and user_chats.chats_id = chat.id\r\n" + 
			"	and message.is_read = 0\r\n" + 
			"	and chat.id = ?2\r\n" + 
			"	and user_chats.users_involved_id = ?1\r\n" + 
			"	and message.author_id <> ?1", nativeQuery = true)		
	int getUnreadOfChat(int userid, int chatid);	
	
	Chat findById(int chatId);	
	void delete(Chat chat);
}
