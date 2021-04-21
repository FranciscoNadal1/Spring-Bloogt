package com.blog.project.app.models.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction;
import com.blog.project.app.entities.reaction.PostReaction;
import com.blog.project.app.entities.reaction.Reaction;
import com.blog.project.app.models.dao.ICommentReaction;
import com.blog.project.app.models.dao.IComments;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.IPostReaction;

@Service
public class ReactionServiceImpl implements IReactionService {

	@Autowired
	IUserService userService;

	@Autowired
	ICommentReaction commentReactionDao;
	
	@Autowired
	IPostReaction postReactionDao;	

	@Autowired
	IPost postDao;
	
	@Autowired
	IComments commentsDao;



	@Transactional
	@Override
	public void likeOrDislikePostOrComment(User user, int id, boolean likeOrDislike, String postOrComment) {
		try {
			Post post = null;
			Comments comment = null;
			Reaction reaction = null;
			User loggedUser = user;		
			
			
			if(postOrComment.equals("Post")) {				
				post = postDao.findPostById(id);
				reaction =  postReactionDao.findByPostAndReactedBy(post, loggedUser);	
			}
			
			if(postOrComment.equals("Comment")) {
				comment = commentsDao.findCommentsById(id);			
				reaction =  commentReactionDao.findByCommentAndReactedBy(comment, loggedUser);					
			}			
			
			if(reaction == null) {

				if(postOrComment.equals("Post"))
					reaction =  new PostReaction();
				if(postOrComment.equals("Comment"))
					reaction =  new CommentReaction();
				reaction.setCreatedAt(new Date());
				
			}

			reaction.setReactedBy(loggedUser);
			reaction.setReaction(likeOrDislike); 
			
			if(postOrComment.equals("Post")) {
				PostReaction castedPostReaction = (PostReaction) reaction;
				castedPostReaction.setPost(post);

				postReactionDao.save(castedPostReaction);
			}
			if(postOrComment.equals("Comment")) {
				CommentReaction castedCommentReaction = (CommentReaction) reaction;
				castedCommentReaction.setComment(comment);
				
				commentReactionDao.save(castedCommentReaction);				
			}
				
			userService.save(loggedUser);		
			}catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Reaction could not be applied");
			}
	}
	
	@Override
	public void likeOrDislikePost(int postId, boolean likeOrDislike) {		

		User loggedUser = userService.getLoggedUser();
		this.likeOrDislikePostOrComment(loggedUser, postId, likeOrDislike, "Post");		
	}
	
	@Override
	public void likeOrDislikeComment(int commentId, boolean likeOrDislike) {
		User loggedUser = userService.getLoggedUser();
		this.likeOrDislikePostOrComment(loggedUser, commentId, likeOrDislike, "Comment");		
	}	
	
	/*
	public int getPuntuation(int postId) {
		Post post = postDao.findPostById(postId);
		
		return 10;
	}
*/
	@Override
	public int getDislikesPost(Post post) {
		return	postReactionDao.countByPostAndReactionFalse(post);
	}

	@Override
	public int getLikesPost(Post post) {
		return	postReactionDao.countByPostAndReactionTrue(post);
	}

	@Override
	public int getDislikesComment(Comments post) {
		return commentReactionDao.countByCommentAndReactionFalse(post);
	}

	@Override
	public int getLikesComment(Comments post) {
		return commentReactionDao.countByCommentAndReactionTrue(post);
	}

}
