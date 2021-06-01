package com.blog.project.app.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.SharedPost.SharedPostProjection;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.dao.ISharedPost;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class TestChamberController {

	@Autowired
	ISharedPost sharedPostDao;

	@Autowired
	IUserService userService;

	@Autowired
	private JWTHandler jwtHandler;
	
	@GetMapping("/listRetweets")
	public List<SharedPostProjection> searchUser(HttpServletRequest request, @RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();
		
		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		//System.out.println(sharedPostDao.getRetweets());
		//responseJson.appendField("retweets", sharedPostDao.getRetweets());
/*
		return sharedPostDao.findPostOfSharedPostBySharedBy(authenticatedUser);*/
		return null;
	}
}
