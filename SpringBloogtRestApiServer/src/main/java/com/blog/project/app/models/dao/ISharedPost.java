package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.SharedPost;
import com.blog.project.app.entities.SharedPost.SharedPostProjection;
import com.blog.project.app.entities.User;

public interface ISharedPost   extends BaseRepository<SharedPost, Long> {
/*
	@Query("SELECT post FROM SharedPost")
	List<showPosts> getRetweets();
	*/
/*
	@Query("SELECT * FROM Post")
	List<Post> getRetweets();
 */
	List<SharedPostProjection> findPostOfSharedPostBySharedByIn(List<User> users);
	
	
}

