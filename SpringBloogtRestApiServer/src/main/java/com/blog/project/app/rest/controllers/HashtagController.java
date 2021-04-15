package com.blog.project.app.rest.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.IHashtagService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/hashtags")
public class HashtagController {
	
	private static final Logger logger = LoggerFactory.getLogger(HashtagController.class);

	@Autowired
	private IHashtagService hashtagService;
	
	@Autowired
	private JWTHandler jwtHandler;

	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		GET Methods
///////////
///////////		Methods to retrieve information

	@GetMapping("/getAllHashtags")
	public List<HashtagShow> getAllHashtags(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<HashtagShow> returningJSON = hashtagService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
	

	@GetMapping("/getHashtagById/{id}")
	public List<HashtagShow> getHashtagById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		List<HashtagShow> returningJSON = hashtagService.findOne(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getPostsOfHashtagById/{id}")
	public List<PostsOfHashtag> getPostsOfHashtagById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		List<PostsOfHashtag> returningJSON = hashtagService.findAllPostsOfHashtagById(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}	

	@GetMapping("/getPostsOfHashtagByName/{name}")
	public PostsOfHashtag getPostsOfHashtagByName(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "name") String name) {
		response.setContentType(contentType);

		PostsOfHashtag returningJSON = hashtagService.findPostOfHashtagByName(name);

		if (returningJSON.equals(null))
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////
///////////		Methods to create new information
	

	@PostMapping("/newHashtag")
	public JSONObject createHashtag(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, Object> payload, @RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();
		
		if(payload.isEmpty())
			throw new NoPayloadDataException();
		
		response.setContentType(contentType);


		
		Hashtag newHashtag = new Hashtag();

		String hashtagName = (String) payload.get("name");
		newHashtag.setName(hashtagName);

		hashtagService.save(newHashtag);

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "Hashtag created : " + hashtagName);
		return responseJson;
	}
	
	
	
	
	
}
