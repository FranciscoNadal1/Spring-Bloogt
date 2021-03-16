package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateAsc;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateDesc;
import com.blog.project.app.entities.Post.showPosts;

public interface IPost extends BaseRepository<Post, Long> {

	PostDetails findById(int id);
	
	PostDetailsCommentsSortByDateAsc findByIdOrderByCreatedAtAsc(int id);
	PostDetailsCommentsSortByDateDesc findByIdOrderByCreatedAtDesc(int id);

	Post findReturnPostById(int id);

	List<Post> findAll();

	List<showPosts> findAllProjectedBy();


	List<showPosts> findAllProjectedByOrderByCreatedAtAsc();
	List<showPosts> findAllProjectedByOrderByCreatedAtDesc();
	//List<Post> findAllByOrderByIdAsc();
}
