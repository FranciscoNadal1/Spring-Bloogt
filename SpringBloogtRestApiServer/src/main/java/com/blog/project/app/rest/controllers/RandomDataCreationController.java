package com.blog.project.app.rest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.Reaction;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;
import com.blog.project.app.scheduled.CreateRandomData;
import com.blog.project.app.scheduled.SimulateActivity;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/random")
public class RandomDataCreationController {

	@Autowired
	private JWTHandler jwtHandler;

	@Autowired
	IUserService userService;
	
	@Autowired
	CreateRandomData createRandomData;
	
	@PutMapping("/create/bot")
	public JSONObject createRandomBot(
			HttpServletResponse response, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		

		responseJson.appendField("status", "OK");
		
		responseJson.appendField("message", "You created random bot"); 
		responseJson.appendField("createdUser", createRandomData.createBot()); 

		return responseJson;
	}
	
	
	@PutMapping("/create/post/randomBot")
	public JSONObject createRandomPostRandomBot(
			HttpServletResponse response, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		

		responseJson.appendField("status", "OK");

		Post newPost = createRandomData.createRandomPostsForBot();
		responseJson.appendField("message", "Bot created random post"); 
		responseJson.appendField("createdPostContent", newPost.getContent()); 
		responseJson.appendField("createdPostId", newPost.getId()); 
		responseJson.appendField("userPosted", newPost.getCreatedBy().getUsername()); 

		return responseJson;
	}
	
	
	@PutMapping("/create/comment/randomBot")
	public JSONObject createRandomCommentRandomBot(
			HttpServletResponse response, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		

		responseJson.appendField("status", "OK");
		
		Comments newComment = createRandomData.randomCreateComment();
		responseJson.appendField("message", "Bot created random comment"); 
		responseJson.appendField("createdPostId", newComment.getPost().getId()); 
		responseJson.appendField("createdPostContent", newComment.getPost().getContent()); 
		responseJson.appendField("createdCommentContent", newComment.getMessage()); 
		responseJson.appendField("createdCommentId", newComment.getId()); 
		responseJson.appendField("userCommented", newComment.getCreatedBy().getUsername()); 

		return responseJson;
	}
	
	@PutMapping("/create/reaction/comment/randomBot")
	public JSONObject reactToRandomComentOfRandomBot(
			HttpServletResponse response, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		

		responseJson.appendField("status", "OK");
		
		createRandomData.randomReactToCommentOfBot();
		responseJson.appendField("message", "Bot reacted to a random comment"); 

		return responseJson;
	}
	
	
	@PutMapping("/create/reaction/post/randomBot")
	public JSONObject reactToRandomPostOfRandomBot(
			HttpServletResponse response, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		

		responseJson.appendField("status", "OK");
		
		createRandomData.randomReactToPostOfBot();
		responseJson.appendField("message", "Bot reacted to a random post"); 

		return responseJson;
	}
}
