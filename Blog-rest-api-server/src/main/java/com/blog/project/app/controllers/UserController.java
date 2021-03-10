package com.blog.project.app.controllers;

import java.net.http.HttpHeaders;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.models.service.IUserService;

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
		//Integer i = id != null ? id.intValue() : null;
		return (List<UserData>) userService.findOne(id);

	}

	@GetMapping("/getUserByUsername/{username}")
	public List<UserData> getUserByUsername(HttpServletResponse response, @PathVariable(value = "username") String username) {
		response.setContentType(contentType);
		return userService.findByUsername(username);

	}

}
