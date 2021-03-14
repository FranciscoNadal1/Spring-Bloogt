package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;

public interface IPostService {
/// Default
	public List<Post> findAll();
	public void save(Post post);
	public void delete(Long id);

/// Custom
	public List<PostDetails> findOne(int id);
	public List<showPosts> findAllProjectedBy();
	public Post findReturnPostById(int id);
}
