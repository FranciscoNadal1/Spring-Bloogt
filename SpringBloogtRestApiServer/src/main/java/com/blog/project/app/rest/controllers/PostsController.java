package com.blog.project.app.rest.controllers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;
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
@RequestMapping("/api/posts")
public class PostsController {
	
	@Autowired
	private JWTHandler jwtHandler;
	
	private static final Logger logger = LoggerFactory.getLogger(PostsController.class);

	@Autowired
	private IPostService postService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IHashtagService hashtagService;

	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		GET Methods
///////////
///////////		Methods to retrieve information


	@GetMapping("/getAllPosts")
	public List<showPosts> getAllPosts(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<showPosts> returningJSON = postService.findAllPostsProjection();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getPostById/{id}")
	public PostDetails getPostById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		PostDetails returningJSON = postService.findPostById(id);

		if (returningJSON.equals(null))
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////
///////////		Methods to create new information

	@PostMapping("/newPost")
	public JSONObject createPost(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> payload, @RequestHeader(value="Authorization", required=false) String authorization) {
		
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER")  )
			throw new UnauthorizedArea();
		
		
		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));

		if (payload.isEmpty())
			throw new NoPayloadDataException();

		response.setContentType(contentType);

		Post newPost = new Post();

//		Can upload the category of the post by ID or by the category name, depending if they are a number or a string
			String categoryNameOrId = (String) payload.get("category");
//		Can upload the category of the post by ID or by the category name, depending if they are a number or a string
				Category categoryOfPost = null;
				if (LocalUtils.isNumeric(categoryNameOrId)) {
					categoryOfPost = categoryService.findCategoryById(Integer.parseInt(categoryNameOrId));
					newPost.setCategory(categoryOfPost);
				}
				else {
					categoryOfPost = categoryService.findCategoryByName(categoryNameOrId);
					newPost.setCategory(categoryOfPost);
				}
				if(categoryOfPost == null) 
					throw new RuntimeException("Post must have a category");
				
				
		
		
		
		String postTitle = (String) payload.get("title");
		newPost.setTitle(postTitle);
		newPost.setContent((String) payload.get("content"));
		newPost.setTimesViewed(0);
		newPost.setCreatedAt((Date) LocalUtils.getActualDate());
		newPost.setCreatedBy(authenticatedUser);
		newPost.setImagePost((String) payload.get("imagePost"));



		postService.savePost(newPost);

//	We have to add the hashtag after we have already saved the post, we can add the hashtag by Id or By Name, 
//	and the hashtag will be created  if it's a name and doesn't exist
		List<Hashtag> hashtagList = new LinkedList<Hashtag>();
		int idPost = newPost.getId();
		for (String hashtag : (List<String>) payload.get("hashtags")) {
			Hashtag hash = null;

			try {
				if (LocalUtils.isNumeric(hashtag))
					hash = hashtagService.findHashtagById(Integer.parseInt(hashtag));
				else
					hash = hashtagService.findHashtagByName(hashtag);

				hash.getPosts().add(newPost);
				hashtagService.save(hash);

			} catch (NullPointerException e) {
				if (LocalUtils.isNumeric(hashtag))
					throw new RuntimeException("You can't create a hashtag that is only a number");

				hash = new Hashtag(hashtag, newPost);
				hashtagService.save(hash);
			} catch (Exception e) {
				throw new RuntimeException("Error with the hashtag provided");
			}
		}

		newPost = null;

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "Post created : " + postTitle);

		return responseJson;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		DELETE Methods
///////////
///////////		Methods to update information

	@DeleteMapping("/deletePost/{id}")
	@Transactional
	public JSONObject deletePostById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id, @RequestHeader(value="Authorization", required=false) String authorization) {


		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_MODERATOR", "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		response.setContentType(contentType);
		Post postToDelete = postService.findReturnPostById(id);
		JSONObject responseJson = new JSONObject();
		postService.deletePostById(id);

		try {
			responseJson.appendField("status", "OK");
			responseJson.appendField("message", "eliminated post : '" + postToDelete.getTitle() + "' and all related comments"); 

			return responseJson;
		} catch (NullPointerException e) {
			throw new RuntimeException("There is no post with that Id");
		}
	}
}