package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction.ReactionCommentByUser;
import com.blog.project.app.entities.reaction.PostReaction;
import com.blog.project.app.entities.reaction.PostReaction.ReactionPostByUser;
import com.blog.project.app.entities.reaction.Reaction.ReactionData;

public interface IPostReaction  extends BaseRepository<PostReaction, Long> {

	PostReaction findById(int id);
	
	PostReaction findByPostAndReactedBy(Post post, User user);
	
	int countByPostAndReactionTrue(Post post);
	int countByPostAndReactionFalse(Post post);
	
	ReactionData findByPost(Post post);

	List<ReactionPostByUser> findByReactedBy(User user);
	List<ReactionCommentByUser> findCommentByReactedBy(User user);


	List<ReactionPostByUser> findByReactionAndReactedBy(boolean reaction, User user);
	List<ReactionPostByUser> findByReactionTrue();
	List<ReactionPostByUser> findByReactionFalse();
	
	
}
