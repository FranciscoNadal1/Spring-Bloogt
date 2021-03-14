package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;

public interface IPost extends BaseRepository<Post, Long> {

	List<PostDetails> findById(int id);

	Post findReturnPostById(int id);

	Iterable<Post> findAll();

	List<showPosts> findAllProjectedBy();

	
}
