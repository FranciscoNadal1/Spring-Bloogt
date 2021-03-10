package com.blog.project.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.models.service.IUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private IUserService userService;

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
	public List<User> getAllUsers() {
		return userService.findAll();
	}

	@GetMapping("/getUserByMail/{mail}")
	public List<UserData> getUserByMail(@PathVariable(value = "mail") String email) {
		return userService.findByEmail(email);

	}

	@GetMapping("/getUserById/{id}")
	public List<User> getUserById(@PathVariable(value = "id") Long id) {
		return (List<User>) userService.findOne(id);

	}

	@GetMapping("/getUserByUsername/{username}")
	public List<UserData> getUserByUsername(@PathVariable(value = "username") String username) {
		return userService.findByUsername(username);

	}

}
