package com.blog.project.app.rest.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.models.dao.IHashtag;
import com.blog.project.app.models.dao.IUser;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/search")
public class SearchController {


	@Autowired
	IUser userDao;
	
	@Autowired
	IHashtag hashtagDao;
	
	@GetMapping("/user/{usernamePart}/{results}")
	public JSONObject searchUser(HttpServletRequest request, 
			@PathVariable(value = "usernamePart") String usernamePart, 
			@PathVariable(value = "results") int results) {
		
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("listUsersMatch", userDao.searchUsersByUsername(usernamePart,results));

		return responseJson;
	}
	@GetMapping("/hashtag/{hashtagPart}/{results}")
	public JSONObject searchHashtag(HttpServletRequest request, 
			@PathVariable(value = "hashtagPart") String hashtagPart, 
			@PathVariable(value = "results") int results) {
		
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("listHashtagMatch", hashtagDao.searchHashtagByString(hashtagPart,results));

		return responseJson;
	}
}
