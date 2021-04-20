package com.blog.project.app.models.dao;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.CommentReaction;
import com.blog.project.app.entities.reaction.PostReaction;
import com.blog.project.app.entities.reaction.Reaction;

public interface ICommentReaction  extends BaseRepository<CommentReaction, Long> {

	CommentReaction findById(int id);
	
	CommentReaction findByCommentAndReactedBy(Comments comment, User user);
	
	int countByCommentAndReactionTrue(Comments post);
	int countByCommentAndReactionFalse(Comments post);
}
