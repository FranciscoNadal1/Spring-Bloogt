package com.blog.project.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.models.service.IUserService;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private IUserService userService;

	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////
	/*
	 * GET METHODS
	 * 
	 * /getAllUsers 
	 * /getUserByMail/{mail} 
	 * /getUserById/{id}
	 * /getUserByUsername/{username}
	 * 
	 */
//////////////////////////////////////////////////////////////////////

	@GetMapping("/getAllUsers")
	public List<UserData> getAllUsers(HttpServletResponse response) {
		response.setContentType(contentType);
		return userService.findAllProjectedBy();
	}

	@GetMapping("/getUserByMail/{mail}")
	public List<UserData> getUserByMail(HttpServletResponse response, @PathVariable(value = "mail") String email) {
		response.setContentType(contentType);
		return userService.findByEmail(email);
	}

	@GetMapping("/getUserById/{id}")
	public List<UserData> getUserById(HttpServletResponse response, @PathVariable(value = "id") int id) {
		response.setContentType(contentType);
		return (List<UserData>) userService.findOne(id);

	}

	@GetMapping("/getUserByUsername/{username}")
	public List<UserData> getUserByUsername(HttpServletResponse response,
			@PathVariable(value = "username") String username) {
		response.setContentType(contentType);
		return userService.findByUsername(username);

	}
	
	
///////////////////////////////////////////////////////////////////////
/*
* POST METHODS
* 
* 
*/
//////////////////////////////////////////////////////////////////////


	@PostMapping("/newUser")
	public JSONObject  createUser(@RequestBody Map<String, Object> payload) {
		System.out.println(payload.get("message"));
		User newUser = new User();
		

		newUser.setName((String) payload.get("name"));
		newUser.setUsername((String) payload.get("username"));
		newUser.setRole((String) payload.get("role"));
		newUser.setAvatar((String) payload.get("avatar"));
		newUser.setSurname((String) payload.get("surname"));
		newUser.setPassword((String) payload.get("password"));
		newUser.setCreatedAt((String) payload.get("createdAt"));
		newUser.setEmail((String) payload.get("email"));
		
		userService.save(newUser);
		
		JSONObject responseJson= new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "user created");
		return responseJson;
	}
	
}
