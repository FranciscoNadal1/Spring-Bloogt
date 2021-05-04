package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction.ReactionCommentByUser;
import com.blog.project.app.entities.reaction.PostReaction.ReactionPostByUser;
import com.blog.project.app.entities.reaction.Reaction.ReactionData;

public interface IReactionService {

	
	public int getDislikesPost(Post post);
	public int getLikesPost(Post post);
	

	public int getDislikesComment(Comments post);
	public int getLikesComment(Comments post);

	public void likeOrDislikePostOrComment(User user, int id, boolean likeOrDislike, String postOrComment);
	
	
	public ReactionData findReactionsByPost(Post post);
	public ReactionData findReactionsByComment(Comments post);
	
	public List<ReactionPostByUser> getPostReactionsOfUser(User user);
	public List<ReactionCommentByUser> getCommentReactionsOfUser(User user);
	public List<ReactionCommentByUser> getCommentReactionUpOrDownOfUser( boolean reaction, User user);
	public List<ReactionPostByUser> getPostReactionUpOrDownOfUser( boolean reaction, User user);
	
	// For logged user - Embed blog
	public void likeOrDislikePost(int postId, boolean likeOrDislike);
	public void likeOrDislikeComment(int commentId, boolean likeOrDislike);
	///////////////////////////////	
}
