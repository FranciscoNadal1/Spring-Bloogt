package com.blog.project.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;

//@RequestMapping("/website")
@Controller 
public class UserManagement {

	@Autowired
	private IUserService userService;


	

	@GetMapping("/userlist")
	public String listUsers(Map<String, Object> model) {
		model.put("titulo", "List of users" );
		

		List<UserData> returningJSON = userService.findAllProjectedBy();

		model.put("users", returningJSON );
		
		return "listofusers";
	}
	@GetMapping("/profile/{username}")
	public String userProfile(Map<String, Object> model,
			@PathVariable(value = "username") String username) {
		model.put("titulo", "Profile" );
		
		UserData returningJSON = userService.findByUsername(username);		
		int idUser = Integer.parseInt(returningJSON.getId());
		
		List<UserComments> allCommentsOfUser 	= 	userService.findAllCommentsOfUserProjectedById(idUser);
		List<UserPosts> allPostsOfUser 			= 	userService.findAllPostsOfUserProjectedById(idUser);
		
		model.put("user", returningJSON );
		model.put("comments", allCommentsOfUser );
		model.put("posts", allPostsOfUser );
		
		System.out.println(returningJSON.getEmail());
		
		return "profile";
	}
}
