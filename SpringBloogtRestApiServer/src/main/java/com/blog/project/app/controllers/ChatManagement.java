package com.blog.project.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.IChatMessageService;
import com.blog.project.app.models.service.IHashtagService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.controllers.CategoryController;
import com.blog.project.app.utils.LocalUtils;

@Controller
public class ChatManagement {

	@Autowired
	private LocalUtils utils;
	
	@Autowired
	private IChatMessageService chatService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IHashtagService hashtagService;

	private static final Logger logger = LoggerFactory.getLogger(ChatManagement.class);
	
	@GetMapping(value={"/newchat/{username}"})
	public String chat(Model model, @PathVariable(value = "username") String username) {
		
		//TODO implement new chat window		

		model.addAttribute("username", username);
		model.addAttribute("titulo", "Sending message to :" + username);
		utils.addDataToMenu(model, categoryService, hashtagService);
		return "newchat";
	}
	@GetMapping("/chatList")
	public String getChatList (Model model) {
		
		User loggedUser = null;
		String username = null;
		try {
		 loggedUser = userService.getLoggedUser();
		 username = loggedUser.getUsername();
			 
		}catch(Exception e) {
			throw new RuntimeException("You need to log in");
		}
		
		if(!loggedUser.getUsername().equals(username))
			throw new RuntimeException("You can't see the messages of other users");		

		List<Chat> listOfChats = chatService.findAllChatsOfLoggedUser();

		model.addAttribute("titulo", "List of chats of "+ username);
		model.addAttribute("chatList", listOfChats);
		
		Map<Integer,Integer> chatNumberUnread = new HashMap<Integer,Integer>();
		
		for(Chat chat : listOfChats) {
			int numberOfUnreadMessages = chatService.unreadMessagesFromChat(chat);
			chatNumberUnread.put(chat.getId(), numberOfUnreadMessages);
		}
		

		model.addAttribute("mapChatNumberUnread", chatNumberUnread);
		
		

		utils.addDataToMenu(model, categoryService, hashtagService);
		return "/profile/chatList";	
	}	
	
	@GetMapping("/chat/{chatId}")
	public String chatList (Model model, @PathVariable(value = "chatId") int chatId) {
		
		User loggedUser = null;
		String username = null;
		try {
		 loggedUser = userService.getLoggedUser();
		 username = loggedUser.getUsername();

			 
		}catch(Exception e) {
			throw new RuntimeException("You need to log in");
		}
		
		Chat chat = chatService.findChatById(chatId);
		
		if(!chat.getUsersInvolved().contains(loggedUser))			
			throw new RuntimeException("You can't see the messages of other users");		
		
		List<User> listOfUsersInvolved = chat.getUsersInvolved();
		
		User chattingWith = null;
		for(User us : listOfUsersInvolved)
			if(!us.getUsername().equals(username)) 
				chattingWith=us;

		model.addAttribute("titulo", "Chat with "+ chattingWith.getUsername());
		model.addAttribute("chat", chat);
		model.addAttribute("chattingWith", chattingWith.getUsername());
		utils.addDataToMenu(model, categoryService, hashtagService);
		chatService.allMessagesToReadExceptAuthored(chat, loggedUser);
		
		return "/profile/chat";	
	}	

	@PostMapping("/sendMessageToChat/{chatId}")
	public String createNewChat(HttpServletRequest request, Model model, @PathVariable(value = "chatId") int chatId) {
		
		User loggedUser = null;
		try {
		 loggedUser = userService.getLoggedUser();
		}catch(Exception e) {
			throw new RuntimeException("You need to be logged in");
		}
		
		chatService.newMessageToChat(loggedUser, request.getParameter("answer"), chatId);
		utils.addDataToMenu(model, categoryService, hashtagService);
		
		return "redirect:/chat/"+chatId+"";
	}
		
	@PostMapping(value={"/newmessage/{username}"})
	public String createNewChat(HttpServletRequest request, Model model, @PathVariable(value = "username") String username) {
		
		User toUser = userService.getUserByUsername(username);

		User loggedUser = null;
		try {
		 loggedUser = userService.getLoggedUser();
		}catch(Exception e) {
			throw new RuntimeException("You need to be logged in");
		}
		
		String messageStr = request.getParameter("message");

		logger.info("User:" + toUser + " just created a chat with " + toUser);

		int chatId = chatService.newChatFromTo(loggedUser, messageStr, toUser);
		utils.addDataToMenu(model, categoryService, hashtagService);
		

		return "redirect:/chat/"+chatId+"";
		
	}	
}
