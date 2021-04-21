package com.blog.project.app.rest.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Comments.ShowAllComments;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.errors.InvalidPayloadException;
import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {
	private static final Logger logger = LoggerFactory.getLogger(CommentsController.class);

	@Autowired
	private ICommentsService commentsService;

	@Autowired
	private IPostService postService;
	
	@Autowired
	private IUserService userService;

	@Autowired
	private JWTHandler jwtHandler;
	
	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		GET Methods
///////////
///////////		Methods to retrieve information

	
	@GetMapping("/getAllComments")
	public List<ShowAllComments> getAllComments(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<ShowAllComments> returningJSON = commentsService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getCommentById/{id}")
	public List<ShowAllComments> getCommentById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		List<ShowAllComments> returningJSON = commentsService.findOne(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////
///////////		Methods to create new information
	

	@PostMapping("/newComment")
	public JSONObject createComment(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, Object> payload, @RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();
		
		if(payload.isEmpty())
			throw new NoPayloadDataException();
		
		response.setContentType(contentType);

		int postId;
		int userId;
		try {
		     postId = (int) payload.get("post_id");
		     userId = (int) payload.get("user_id");
		     
		} catch (Exception e) {
		    throw new InvalidPayloadException();
		}

		Comments newComment = new Comments();

		String categoryName = (String) payload.get("name");
		newComment.setCreatedAt( LocalUtils.getActualDate());
		
		newComment.setMessage((String)payload.get("message"));
		//////////////////////////////////////////////
		
		newComment.setCreatedBy(userService.findReturnUserById(userId));		
		newComment.setPost(postService.findReturnPostById(postId));

		commentsService.save(newComment);

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "Comment created : " + categoryName);
		return responseJson;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////
///////////		Methods to create new information
	
	@PutMapping("/banComment/{idComment}")
	public JSONObject banComment(
			HttpServletRequest request, 
			Model model, 
			@PathVariable(value = "idComment") int idComment,
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_MODERATOR"))
			throw new UnauthorizedArea();
		
	
		Comments comment = commentsService.findCommentById(idComment);
		
			comment.setRemovedByModerator(true);
			commentsService.save(comment);
			
			JSONObject responseJson = new JSONObject();
			responseJson.appendField("status", "OK");
			responseJson.appendField("message", "Comment is banned now");
			
			return responseJson;
	}
	
}
