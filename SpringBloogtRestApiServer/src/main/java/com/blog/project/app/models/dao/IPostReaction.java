package com.blog.project.app.models.dao;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.reaction.PostReaction;

public interface IPostReaction  extends BaseRepository<PostReaction, Long> {

	PostReaction findById(int id);
	
	PostReaction findByPostAndReactedBy(Post post, User user);
	
	int countByPostAndReactionTrue(Post post);
	int countByPostAndReactionFalse(Post post);
	
}
