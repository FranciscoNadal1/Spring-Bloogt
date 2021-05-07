package com.blog.project.app.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction.ReactionCommentByUser;
import com.blog.project.app.entities.reaction.PostReaction.ReactionPostByUser;
import com.blog.project.app.entities.reaction.Reaction;
import com.blog.project.app.entities.reaction.Reaction.ReactionData;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IReactionService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/reaction")
public class ReactionController {

	@Autowired
	IReactionService commentReactionService;

	@Autowired
	IPostService postService;
	
	@Autowired
	ICommentsService commentService;

	@Autowired
	private JWTHandler jwtHandler;

	@Autowired
	IUserService userService;

	@GetMapping("/posts/user/{username}")
	public List<ReactionPostByUser> postsReactionsOfUser(
			@RequestHeader(value="Authorization", required=false) String authorization,
			@PathVariable(value = "username") String username,
			@RequestParam(required = false) boolean reaction) {

		User user = userService.getUserByUsername(username);
		
		List<ReactionPostByUser> reactionPostData = null;		

		if(reaction == true) {
			reactionPostData = commentReactionService.getPostReactionUpOrDownOfUser(true,user);
			return reactionPostData;
		}
		if(reaction == false) {
			reactionPostData = commentReactionService.getPostReactionUpOrDownOfUser(false,user);
			return reactionPostData;			
		}
		
		reactionPostData = commentReactionService.getPostReactionsOfUser(user);
		
		if(reactionPostData == null) 
			throw new RuntimeException("This user has no reactions");
		
		return reactionPostData;
	}
	@GetMapping("/comments/user/{username}")
	public List<ReactionCommentByUser> commentReactionsOfUser(
			@RequestHeader(value="Authorization", required=false) String authorization,
			@PathVariable(value = "username") String username,
			@RequestParam(required = false) boolean reaction) {

		User user = userService.getUserByUsername(username);
		
		List<ReactionCommentByUser> reactionCommentData = null;
		System.out.println("------------");
		System.out.println(reaction);
		
			if(reaction == true) {
				reactionCommentData = commentReactionService.getCommentReactionUpOrDownOfUser(true,user);
				return reactionCommentData;
			}
			if(reaction == false) {
				reactionCommentData = commentReactionService.getCommentReactionUpOrDownOfUser(false,user);
				return reactionCommentData;
			}			
			

			reactionCommentData = commentReactionService.getCommentReactionsOfUser(user);

		
		if(reactionCommentData == null) 
			throw new RuntimeException("This user has no reactions");
		
		return reactionCommentData;
	}	
	
	
	
	@GetMapping("/comment/user")
	public List<ReactionCommentByUser> commentReactionsLoggedUser(@RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		
		List<ReactionCommentByUser> reactionData = commentReactionService.getCommentReactionsOfUser(authenticatedUser);
		if(reactionData == null) 
			throw new RuntimeException("This user has no reactions");
		
		return reactionData;
	}

	@GetMapping("/post/user")
	public List<ReactionPostByUser> postReactionsLoggedUser(@RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		
		
		List<ReactionPostByUser> reactionData = commentReactionService.getPostReactionsOfUser(authenticatedUser);
		if(reactionData.isEmpty()) 
			throw new RuntimeException("This user has no reactions");
		
		return reactionData;
	}
	
	
	
	@GetMapping("/comment/{commentId}")
	public ReactionData reactToComment(@PathVariable(value = "commentId") int commentId) {
		Comments comment = commentService.findCommentById(commentId);
		if(comment == null) 
			throw new RuntimeException("There is no comment with that Id");
		
		ReactionData reactionData = commentReactionService.findReactionsByComment(comment);
		if(reactionData == null) 
			throw new RuntimeException("This comment has no reactions");
		
		return reactionData;
	}

	@GetMapping("/post/{postId}")
	public ReactionData reactToPost(@PathVariable(value = "postId") int postId) {
		Post post = postService.findReturnPostById(postId);
		if(post == null) 
			throw new RuntimeException("There is no post with that Id");
		
		ReactionData reactionData = commentReactionService.findReactionsByPost(post);
		if(reactionData == null) 
			throw new RuntimeException("This post has no reactions");
		
		return reactionData;
	}	
	
	@PutMapping("/comment/{idComment}/{reaction}")
	public JSONObject reactToComment(
			HttpServletResponse response, 
			HttpServletRequest request,
			@PathVariable(value = "idComment") int idComment, 
			@PathVariable(value = "reaction") String reaction, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		
		try {			
			if(reaction.equals("like")) {
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idComment, true, "Comment");
			
			}

			if(reaction.equals("dislike")) {
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idComment, false, "Comment");
			
			}
			
			responseJson.appendField("status", "OK");
			responseJson.appendField("message", "You reacted to this comment"); 

			return responseJson;
		} catch (NullPointerException e) {
			throw new RuntimeException("There is no comment with that Id");
		}
	}
	
	@PutMapping("/post/{idPost}/{reaction}")
	public JSONObject reactToPost(
			HttpServletResponse response, 
			HttpServletRequest request,
			@PathVariable(value = "idPost") int idPost, 
			@PathVariable(value = "reaction") String reaction, 
			@RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();

		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		JSONObject responseJson = new JSONObject();
		
		try {			
			if(reaction.equals("like"))
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idPost, true, "Post");

			if(reaction.equals("dislike"))
			commentReactionService.likeOrDislikePostOrComment(authenticatedUser, idPost, false, "Post");
			
			responseJson.appendField("status", "OK");
			responseJson.appendField("message", "You reacted to this post"); 

			return responseJson;
		} catch (NullPointerException e) {
			throw new RuntimeException("There is no post with that Id");
		}
	}	
	
}
