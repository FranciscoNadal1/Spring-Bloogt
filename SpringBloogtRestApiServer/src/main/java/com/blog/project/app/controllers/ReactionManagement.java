package com.blog.project.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.models.dao.IComments;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IReactionService;

@Controller
public class ReactionManagement {

	@Autowired
	IReactionService reactionService;		

	@Autowired
	IComments commentsService;
	
	@GetMapping("/reaction/likePost/{idPost}")
	public String likePost(Model model, @PathVariable(value = "idPost") int idPost) {
		
		reactionService.likeOrDislikePost(idPost, true);

		return "redirect:/post/list/" + idPost;
	}
	
	@GetMapping("/reaction/dislikePost/{idPost}")
	public String dislikePost(Model model, @PathVariable(value = "idPost") int idPost) {
		
		reactionService.likeOrDislikePost(idPost, false);

		return "redirect:/post/list/" + idPost;
	}

	
	@GetMapping("/reaction/likeComment/{idComment}")
	public String likeComment(Model model, @PathVariable(value = "idComment") int idComment) {
		Comments comment = commentsService.findCommentsById(idComment);
		reactionService.likeOrDislikeComment(idComment, true);

		return "redirect:/post/list/" + comment.getPost().getId();
	}
	
	@GetMapping("/reaction/dislikeComment/{idComment}")
	public String dislikeComment(Model model, @PathVariable(value = "idComment") int idComment) {
		Comments comment = commentsService.findCommentsById(idComment);		
		reactionService.likeOrDislikeComment(idComment, false);

		return "redirect:/post/list/" + comment.getPost().getId();
	}
	
	
	
	
	
	
}
