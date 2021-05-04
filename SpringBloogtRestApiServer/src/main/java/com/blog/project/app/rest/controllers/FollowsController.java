package com.blog.project.app.rest.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Role;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.User.UserFollowData;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/follows")
public class FollowsController {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private JWTHandler jwtHandler;

	private String contentType = "application/json";
	
	  
	@GetMapping("/{username}/following")
	public List<OnlyUsername> getFollows(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "username") String username) {
		
		response.setContentType(contentType);

		List<OnlyUsername> user = userService.getProjectionUsersThatAreFollowedByUser(username);


		return user;
	}
	
	@GetMapping("/{username}/followedBy")
	public List<OnlyUsername> getFollowedBy(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "username") String username) {
		response.setContentType(contentType);
		
		List<OnlyUsername> user = userService.getProjectionUsersThatFollowUser(username);

		return user;
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////			POST
/*
	@PostMapping("/follow")
	public JSONObject followUser(HttpServletRequest request, @RequestBody Map<String, Object> payload, @RequestHeader(value="Authorization", required=false) String authorization) {
		System.out.println(payload.get("message"));

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		String username = (String) payload.get("username");
		
		User userToFollow = null;

			userToFollow = userService.getUserByUsername(username);
			
		if(userToFollow == null)
			throw new RuntimeException("User doesn't exist");	

		
		userService.followUserCommon(authenticatedUser, userToFollow.getUsername());
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User followed");
		return responseJson;
	}
*/
	@PostMapping("/follow/{username}")
	public JSONObject followUser(
			HttpServletRequest request, 
			@RequestHeader(value="Authorization", required=false) String authorization,
			@PathVariable(value = "username") String username) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		User userToFollow = null;
		
			

			userToFollow = userService.getUserByUsername(username);
			if(userToFollow.equals(authenticatedUser))
				throw new RuntimeException("You can't follow yourself");	
			
		if(userToFollow == null)
			throw new RuntimeException("User doesn't exist");	

		
		userService.followUserCommon(authenticatedUser, userToFollow.getUsername());
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User followed");
		return responseJson;
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////	DELETE
	
	@DeleteMapping("/unfollow/{username}")
	public JSONObject unfollowUser(
			HttpServletRequest request, 
			@RequestHeader(value="Authorization", required=false) String authorization,
			@PathVariable(value = "username") String username) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		User userToUnfollow = userService.getUserByUsername(username);


		if(userToUnfollow.equals(authenticatedUser))
			throw new RuntimeException("You can't unfollow yourself");	
		
		if(userToUnfollow == null)
			throw new RuntimeException("User doesn't exist");	
		
		userService.unfollowUserCommon(authenticatedUser, userToUnfollow.getUsername());
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User unfollowed");
		return responseJson;
	}
	
	/*
	@DeleteMapping("/unfollow")
	public JSONObject unfollowUser(HttpServletRequest request, @RequestBody Map<String, Object> payload, @RequestHeader(value="Authorization", required=false) String authorization) {
		System.out.println(payload.get("message"));

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		String username = (String) payload.get("username");
		User userToUnfollow = userService.getUserByUsername(username);

		if(userToUnfollow == null)
			throw new RuntimeException("User doesn't exist");	
		
		userService.unfollowUserCommon(authenticatedUser, userToUnfollow.getUsername());
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User unfollowed");
		return responseJson;
	}
	*/
}
