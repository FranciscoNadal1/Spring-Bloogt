package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;

public interface IPost extends BaseRepository <Post, Long> {


	List<PostDetails> findById(int id);
	Iterable<Post> findAll();
	List<showPosts> findAllProjectedBy();

	

}
