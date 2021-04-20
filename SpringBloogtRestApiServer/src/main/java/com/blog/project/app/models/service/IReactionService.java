package com.blog.project.app.models.service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;

public interface IReactionService {

	public void likeOrDislikePost(int postId, boolean likeOrDislike);
	
	public int getDislikesPost(Post post);
	public int getLikesPost(Post post);
	

	public int getDislikesComment(Comments post);
	public int getLikesComment(Comments post);

	void likeOrDislikeComment(int commentId, boolean likeOrDislike);
}
