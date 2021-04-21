package com.blog.project.app.rest.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.entities.chat.Chat.ChatsAndMessageOfUser;
import com.blog.project.app.entities.chat.Chat.ListChatsOfUser;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.IChatMessageService;
import com.blog.project.app.models.service.IReactionService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Autowired
	IReactionService commentReactionService;

	@Autowired
	IChatMessageService chatService;

	@Autowired
	private JWTHandler jwtHandler;

	@Autowired
	IUserService userService;

	
	@GetMapping("/getAllChatsOfUser")
	public List<ListChatsOfUser> getChatsLoggedUser(
			HttpServletResponse response, 
			HttpServletRequest request,@RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		List<ListChatsOfUser> listChats = chatService.findAllChatsOfLoggedUserProjection(authenticatedUser);
		
		return listChats;
	}	
	
	@GetMapping("/getChat/{idChat}")
	public ChatsAndMessageOfUser getChatsLoggedUser(
			HttpServletResponse response, 
			HttpServletRequest request,
			@PathVariable(value = "idChat") int idChat, 
			@RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		Chat chat = chatService.findChatById(idChat);
		if(chat == null)
			throw new RuntimeException("Chat doesn't exist");
		
		if(!chat.getUsersInvolved().contains(authenticatedUser)) {
			throw new RuntimeException("You can't see this chat");			
		}
		
		ChatsAndMessageOfUser listChats = chatService.findChatOfUserProjection(chat, authenticatedUser);
		
		return listChats;
	}		
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////POST


	@PostMapping("/newChat/{username}")
	public JSONObject newChatWithUser(
			HttpServletResponse response, 
			HttpServletRequest request,
			@RequestBody Map<String, Object> payload,
			@PathVariable(value = "username") String username, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		JSONObject responseJson = new JSONObject();		

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));

		User toUser = userService.getUserByUsername(username);

		if(toUser == null)
			throw new RuntimeException("This user doesn't exist");

		String message;
		try {
			message = (String) payload.get("message");
		} catch (Exception e) {
			throw new RuntimeException("You need to specify a correct message");
		}

		int chatId = chatService.newChatFromTo(authenticatedUser, message , toUser);

		//chatService.newMessageToChat(request.getParameter("answer"), chatId);


		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "Chat created with" + username);
		return responseJson;
	}



	@PostMapping("/newMessage/{idChat}")
	public JSONObject sendMessageToChat(
			HttpServletResponse response, 
			HttpServletRequest request,
			@RequestBody Map<String, Object> payload,
			@PathVariable(value = "idChat") int idChat, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		JSONObject responseJson = new JSONObject();		

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		Chat chat = chatService.findChatById(idChat);
		if(chat == null)
			throw new RuntimeException("Chat doesn't exist");

		String message;
		try {
			message = (String) payload.get("message");
		} catch (Exception e) {
			throw new RuntimeException("You need to specify a correct message");
		}

		chatService.newMessageToChat(authenticatedUser, message, chat.getId());

		
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "Message sent to chat");
		return responseJson;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////	DELETE
	
	@DeleteMapping("/delete/chat/{idChat}")
	public JSONObject deleteChat(
			HttpServletResponse response, 
			HttpServletRequest request,
			@PathVariable(value = "idChat") int idChat, 
			@RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		Chat chat = chatService.findChatById(idChat);
		if(chat == null)
			throw new RuntimeException("Chat doesn't exist");
		
		if(!chat.getUsersInvolved().contains(authenticatedUser)) {
			throw new RuntimeException("You can't see this chat");			
		}
		
		Chat chatFound = chatService.findChatById(chat.getId());

		String listUsers = "";
		for(int i=0;i!=chatFound.getUsersInvolved().size();i++) {
			listUsers = listUsers.concat(chatFound.getUsersInvolved().get(i).getUsername());
			if(i!=chatFound.getUsersInvolved().size())
				listUsers = listUsers.concat("-");
		}

		chatService.deleteChat(chatFound);
		JSONObject responseJson = new JSONObject();		
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "Deleted chat between : " + listUsers);
		return responseJson;
	}
	
	
	
	
}
