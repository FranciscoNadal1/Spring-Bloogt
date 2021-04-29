package com.blog.project.app.models.service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;

public interface IReactionService {

	
	public int getDislikesPost(Post post);
	public int getLikesPost(Post post);
	

	public int getDislikesComment(Comments post);
	public int getLikesComment(Comments post);

	public void likeOrDislikePostOrComment(User user, int id, boolean likeOrDislike, String postOrComment);
	
	// For logged user - Embed blog
	public void likeOrDislikePost(int postId, boolean likeOrDislike);
	public void likeOrDislikeComment(int commentId, boolean likeOrDislike);
	///////////////////////////////
}
