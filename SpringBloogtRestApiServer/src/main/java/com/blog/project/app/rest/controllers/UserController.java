package com.blog.project.app.rest.controllers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		GET Methods
///////////

	@GetMapping("/getAllUsers")
	public List<UserData> getAllUsers(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<UserData> returningJSON = userService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getUserByMail/{mail}")
	public List<UserData> getUserByMail(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "mail") String email) {

		response.setContentType(contentType);

		List<UserData> returningJSON = userService.findByEmail(email);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getUserById/{id}")
	public List<UserData> getUserById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		List<UserData> returningJSON = userService.findOne(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getUserByUsername/{username}")
	public UserData getUserByUsername(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "username") String username) {
		response.setContentType(contentType);
		UserData returningJSON = userService.getUserDataByUsername(username);

		if (returningJSON.equals(null))
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
//////////////////////////////////////////////////

	@GetMapping("/getCommentsByUserId/{id}")
	public List<UserComments> getCommentsByUserId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);
		
		List<UserComments> returningJSON = userService.findAllCommentsOfUserProjectedById(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	
	@GetMapping("/getCommentsByUsername/{username}")
	public List<UserComments> getCommentsByUsername(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "username") String username) {
		response.setContentType(contentType);
		
		List<UserComments> returningJSON = userService.findAllCommentsOfUserProjectedByUsername(username);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	
	@GetMapping("/getPostsByUserId/{id}")
	public List<UserPosts> getPostsByUserId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);
		
		List<UserPosts> returningJSON = userService.findAllPostsOfUserProjectedById(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	
	@GetMapping("/getPostsByUsername/{username}")
	public List<UserPosts> getPostsByUserId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "username") String username) {
		response.setContentType(contentType);		
		
		List<UserPosts> returningJSON = userService.findAllPostsOfUserProjectedByUsername(username);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////

	@PostMapping("/newUser")
	public JSONObject createUser(HttpServletRequest request, @RequestBody Map<String, Object> payload) {

		User newUser = new User();

		String username =  (String) payload.get("username");
		if(userService.getUserByUsername(username) == null) 
			newUser.setUsername(username);						
		else
			throw new RuntimeException("Username already taken");
		

		newUser.setName((String) payload.get("name"));
		newUser.setAvatar((String) payload.get("avatar"));
		newUser.setSurname((String) payload.get("surname"));
		
		
		newUser.setPassword(passwordEncoder.encode((String) payload.get("password")));
		
		newUser.setCreatedAt((Date) LocalUtils.getActualDate());	
		
		String email = (String) payload.get("email");
		System.out.println(userService.findByEmail(email));
		if(userService.findByEmail(email).isEmpty()) 
			newUser.setEmail(email);				
		else
			throw new RuntimeException("Email is already in use");
		
		//newUser.setEmail((String) payload.get("email"));
		
		userService.saveUserAndAssignRole(newUser,"ROLE_USER");

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "user "+ newUser.getUsername() +" created");
		return responseJson;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		PUT Methods
///////////	
	

	@PutMapping("/modifyUser/{username}")
	public JSONObject modifyUser(HttpServletRequest request, @RequestBody Map<String, Object> payload, @PathVariable(value = "username") String username) {

		User newUser = userService.getUserByUsername(username);
		if(newUser == null)
			throw new RuntimeException("You did not specify a valid username");


		JSONObject responseJson = new JSONObject();
		
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User "+ newUser.getUsername() +" fields were modified");
		
		JSONObject modifiedFields = new JSONObject();
		List<String> modifiedFieldsStringList = new LinkedList<>();
		
		if(payload.containsKey("name")) {
			newUser.setName((String) payload.get("name"));	
			modifiedFieldsStringList.add("name");
		}
		
		if(payload.containsKey("avatar")) {
			newUser.setAvatar((String) payload.get("avatar"));
			modifiedFieldsStringList.add("avatar");
		}
		
		if(payload.containsKey("surname")) {
			newUser.setSurname((String) payload.get("surname"));
			modifiedFieldsStringList.add("surname");
		}
		
		if(payload.containsKey("email")) {
			String email = (String) payload.get("email");
			if(userService.findByEmail(email).isEmpty()) {
				newUser.setEmail(email);
				modifiedFieldsStringList.add("email");				
			}
			else
				throw new RuntimeException("Email is already in use");
		}
		
		
		if(modifiedFieldsStringList.isEmpty())
			throw new RuntimeException("You did not specify valid fields to modify");
		
		//modifiedFields.appendField("list", modifiedFieldsStringList);
		responseJson.appendField("modifiedFields", modifiedFieldsStringList);
		
		userService.save(newUser);
		
		return responseJson;
	}
	
	
	
	

}
