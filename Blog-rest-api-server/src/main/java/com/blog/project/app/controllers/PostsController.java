package com.blog.project.app.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IPostService postService;
	
	private String contentType = "application/json";
	
	@GetMapping("/getAllPosts")
	public List<showPosts> getAllPosts(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<showPosts> returningJSON = postService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	
	
	@GetMapping("/getPostById/{id}")
	public List<PostDetails> getPostById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		List<PostDetails> returningJSON = postService.findOne(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	
}
