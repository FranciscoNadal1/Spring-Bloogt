package com.blog.project.app.models.service.implementation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction;
import com.blog.project.app.entities.reaction.CommentReaction.ReactionCommentByUser;
import com.blog.project.app.entities.reaction.PostReaction;
import com.blog.project.app.entities.reaction.PostReaction.ReactionPostByUser;
import com.blog.project.app.entities.reaction.Reaction;
import com.blog.project.app.entities.reaction.Reaction.ReactionData;
import com.blog.project.app.models.dao.ICommentReaction;
import com.blog.project.app.models.dao.IComments;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.IPostReaction;
import com.blog.project.app.models.service.INotificationService;
import com.blog.project.app.models.service.IReactionService;
import com.blog.project.app.models.service.IUserService;

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


	@Autowired
	private INotificationService notificationService;

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
				
			}else
				if(reaction.getReaction() == likeOrDislike) {
					if(likeOrDislike)
						throw new RuntimeException("You already liked this comment");
					if(!likeOrDislike)
						throw new RuntimeException("You already disliked this comment");
					
				}
				
			
			reaction.setReactedBy(loggedUser);
			
			reaction.setReaction(likeOrDislike); 
			
			if(postOrComment.equals("Post")) {
				PostReaction castedPostReaction = (PostReaction) reaction;
				castedPostReaction.setPost(post);

				postReactionDao.save(castedPostReaction);
				
				String likedOrDisliked = null;
				if(castedPostReaction.getReaction() == true)
					likedOrDisliked = "like";
				if(castedPostReaction.getReaction() == false)
					likedOrDisliked = "dislike";
				
				notificationService.newNotificationUserObject(likedOrDisliked, post.getCreatedBy(), loggedUser, post);
			}
			if(postOrComment.equals("Comment")) {
				CommentReaction castedCommentReaction = (CommentReaction) reaction;
				castedCommentReaction.setComment(comment);
				
				commentReactionDao.save(castedCommentReaction);		
				
				String likedOrDisliked = null;
				if(castedCommentReaction.getReaction() == true)
					likedOrDisliked = "like";
				if(castedCommentReaction.getReaction() == false)
					likedOrDisliked = "dislike";

				notificationService.newNotificationUserObject(likedOrDisliked, comment.getCreatedBy(), loggedUser, comment);
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

	
	
	
	
	@Override
	public ReactionData findReactionsByPost(Post post) {
		return postReactionDao.findByPost(post);
	}

	@Override
	public ReactionData findReactionsByComment(Comments comment) {
		return commentReactionDao.findByComment(comment);
	}

	@Override
	public List<ReactionPostByUser> getPostReactionsOfUser(User user) {
		return postReactionDao.findByReactedBy(user);
		
	}

	@Override
	public List<ReactionCommentByUser> getCommentReactionsOfUser(User user) {
		return commentReactionDao.findCommentByReactedBy(user);
		
	}
	

	@Override
	public List<ReactionCommentByUser> getCommentReactionUpOrDownOfUser( boolean reaction, User user) {
		return commentReactionDao.findByReactionAndReactedBy(reaction, user);
		
	}	
	
	@Override
	public List<ReactionPostByUser> getPostReactionUpOrDownOfUser( boolean reaction, User user) {
		return postReactionDao.findByReactionAndReactedBy(reaction, user); 
		
	}	
	

}
