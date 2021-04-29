package com.blog.project.app.rest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.dao.ICommentReaction;
import com.blog.project.app.models.service.IReactionService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/reaction")
public class ReactionController {

	@Autowired
	IReactionService commentReactionService;

	@Autowired
	private JWTHandler jwtHandler;

	@Autowired
	IUserService userService;

	@PutMapping("/comment/{idComment}/{reaction}")
	public JSONObject reactToComment(
			HttpServletResponse response, 
			HttpServletRequest request,
			@PathVariable(value = "idComment") int idComment, 
			@PathVariable(value = "reaction") String reaction, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		
		try {			
			if(reaction.equals("like")) {
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idComment, true, "Comment");
			
			}

			if(reaction.equals("dislike")) {
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idComment, false, "Comment");
			
			}
			
			responseJson.appendField("status", "OK");
			responseJson.appendField("message", "You reacted to this comment"); 

			return responseJson;
		} catch (NullPointerException e) {
			throw new RuntimeException("There is no comment with that Id");
		}
	}
	
	@PutMapping("/post/{idPost}/{reaction}")
	public JSONObject reactToPost(
			HttpServletResponse response, 
			HttpServletRequest request,
			@PathVariable(value = "idPost") int idPost, 
			@PathVariable(value = "reaction") String reaction, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		
		try {			
			if(reaction.equals("like"))
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idPost, true, "Post");

			if(reaction.equals("dislike"))
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idPost, false, "Post");
			
			responseJson.appendField("status", "OK");
			responseJson.appendField("message", "You reacted to this post"); 

			return responseJson;
		} catch (NullPointerException e) {
			throw new RuntimeException("There is no post with that Id");
		}
	}	
	
}
