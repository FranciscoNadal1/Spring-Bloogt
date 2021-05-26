package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction;
import com.blog.project.app.entities.reaction.CommentReaction.ReactionCommentByUser;
import com.blog.project.app.entities.reaction.PostReaction.ReactionPostByUser;
import com.blog.project.app.entities.reaction.Reaction.ReactionData;

public interface ICommentReaction  extends BaseRepository<CommentReaction, Long> {

	CommentReaction findById(int id);
	
	CommentReaction findByCommentAndReactedBy(Comments comment, User user);
	
	int countByCommentAndReactionTrue(Comments post);
	int countByCommentAndReactionFalse(Comments post);

	ReactionData findByComment(Comments comment);
	
	List<ReactionPostByUser> findByReactedBy(User user);

	List<ReactionCommentByUser> findCommentByReactedBy(User user);

	List<ReactionCommentByUser> findByReactionAndReactedBy(boolean reaction, User user);

	int countByReactionTrueAndReactedBy(User user);
	int countByReactionFalseAndReactedBy(User user);
}
