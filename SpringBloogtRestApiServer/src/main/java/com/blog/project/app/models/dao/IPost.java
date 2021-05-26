package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateAsc;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateDesc;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;

public interface IPost extends BaseRepository<Post, Long> {

	showPosts findById(int id);
	Post findPostById(int id);
	
	PostDetailsCommentsSortByDateAsc findByIdOrderByCreatedAtAsc(int id);
	PostDetailsCommentsSortByDateDesc findByIdOrderByCreatedAtDesc(int id);

	Post findReturnPostById(int id);

	List<Post> findAll();

	void deleteById(int id);
	
	List<showPosts> findAllProjectedBy();
	List<showPosts> findAllProjectedByCategory(Category category);
	List<showPosts> findAllProjectedByCategoryNot(Category category);


	List<showPosts> findAllProjectedByOrderByCreatedAtAsc();
	List<showPosts> findAllProjectedByOrderByCreatedAtDesc();
	
	

	List<Post> findByCreatedBy(User user);
	List<showPosts> findByCreatedByIn(List<User> listUser);
	List<showPosts> findByCreatedByInAndCategoryOrderByCreatedAtDesc(List<User> listUser, Category category);
	
	
	int countAllByCreatedBy(User user);
	int countAllByCreatedByAndImagePostIsNotNull(User user);
	//List<Post> findAllByOrderByIdAsc();
	
	
}
