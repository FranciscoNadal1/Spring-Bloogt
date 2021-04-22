package com.blog.project.app.rest.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.blog.project.app.entities.Role;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////
	/*
	 * GET METHODS
	 * 
	 * /getAllUsers /getUserByMail/{mail} /getUserById/{id}
	 * /getUserByUsername/{username}
	 * 
	 */
//////////////////////////////////////////////////////////////////////

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


	@GetMapping("/getCommentsByUserId/{id}")
	public List<UserComments> getCommentsByUserId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);
		
		List<UserComments> returningJSON = userService.findAllCommentsOfUserProjectedById(id);

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
///////////////////////////////////////////////////////////////////////
	/*
	 * POST METHODS
	 * 
	 * 
	 */
//////////////////////////////////////////////////////////////////////

	@PostMapping("/newUser")
	public JSONObject createUser(HttpServletRequest request, @RequestBody Map<String, Object> payload) {
		System.out.println(payload.get("message"));
		User newUser = new User();


		
		newUser.setName((String) payload.get("name"));
		newUser.setUsername((String) payload.get("username"));
		newUser.setAvatar((String) payload.get("avatar"));
		newUser.setSurname((String) payload.get("surname"));
		
		
		newUser.setPassword(passwordEncoder.encode((String) payload.get("password")));
		
		newUser.setCreatedAt((Date) LocalUtils.getActualDate());		
		newUser.setEmail((String) payload.get("email"));
		
		userService.saveUserAndAssignRole(newUser,"ROLE_USER");

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "user "+ newUser.getUsername() +" created");
		return responseJson;
	}

}
