package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;

public interface IPostService {
/// Default
	public List<Post> findAllPosts();
	public void savePost(Post post);
	public void deletePost(Long id);

/// Custom

	public showPosts findPostById(int id);
	public List<showPosts> findAllPostsProjection();
	public List<showPosts> findAllPostsProjectionByCategory(Category category);
	public List<showPosts> findAllPostsProjectionByCategoryNot(Category category);
//	public List<showPosts> findAllPostsOfFollowingUser(User user);

	public List<showPosts> findAllPostsOfFollowingUser(List<User> users);
	public List<showPosts> findAllPostsOfFollowingUserAndCategory(List<User> users, Category category);
	public Post findReturnPostById(int id);
	void deletePostById(int id);

	showPosts findPostByIdAndSortByCreatedDateDesc(int id);
	showPosts findPostByIdAndSortByCreatedDateAsc(int id);
	
	public void addVisit(int id);
}
