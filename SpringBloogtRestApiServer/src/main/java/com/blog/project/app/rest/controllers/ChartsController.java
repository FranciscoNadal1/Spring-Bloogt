package com.blog.project.app.rest.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.dao.ICommentReaction;
import com.blog.project.app.models.dao.IComments;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.IPostReaction;
import com.blog.project.app.models.dao.IUser;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/charts")
public class ChartsController {

	@Autowired
	private JWTHandler jwtHandler;
	

	@Autowired
	IUserService userService;

	@Autowired
	IUser userDao;

	@Autowired
	IPost postDao;
	
	@Autowired
	IComments commentDao;
	
	@Autowired
	IPostReaction reactionPostDao;

	@Autowired
	ICommentReaction reactionCommentDao;
	
	@GetMapping("/user/statistics/{username}")
	public JSONObject statisticsUser(
			HttpServletRequest request, 
			@PathVariable(value = "username") String username) {
		
		User user = null;
		
			

			user = userService.getUserByUsername(username);
			
		if(user == null)
			throw new RuntimeException("User doesn't exist");	
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");

		responseJson.appendField("username", user.getUsername());
		responseJson.appendField("avatar", user.getAvatar());
		responseJson.appendField("name", user.getName());
		responseJson.appendField("surname", user.getSurname());		
		responseJson.appendField("totalPosts", postDao.countAllByCreatedBy(user));
		responseJson.appendField("totalComments", commentDao.countAllByCreatedBy(user));
		responseJson.appendField("totalPostsLikes", reactionPostDao.countByReactionTrueAndReactedBy(user));
		responseJson.appendField("totalPostsDislikes", reactionPostDao.countByReactionFalseAndReactedBy(user));
		responseJson.appendField("totalCommentsLikes", reactionCommentDao.countByReactionTrueAndReactedBy(user));
		responseJson.appendField("totalCommentsDislikes", reactionCommentDao.countByReactionFalseAndReactedBy(user));
		responseJson.appendField("totalImages", postDao.countAllByCreatedByAndImagePostIsNotNull(user));
		responseJson.appendField("following", userDao.countFollowedById(user.getId()));
		responseJson.appendField("followers", userDao.countFollowersById(user.getId()));
		return responseJson;
	}
}
