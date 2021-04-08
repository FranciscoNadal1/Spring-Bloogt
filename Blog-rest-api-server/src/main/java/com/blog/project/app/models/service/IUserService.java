package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;

public interface IUserService {
/// Default
	public List<User> findAll();
	public void save(User user);
	public void delete(Long id);
	
/// Custom

	//User basic data
	public List<UserData> findOne(int id);
	public List<UserData> findByEmail(String email);
	public User getUserByUsername(String username);
	public UserData getUserDataByUsername(String username);
//	public User findByUsername(String username);
	public List<UserData> findAllProjectedBy();
	


	public User findReturnUserById(int id);
	
	
	//User Comments
	public List<UserComments> findAllCommentsOfUserProjectedById(int id);

	//User Posts
	public List<UserPosts> findAllPostsOfUserProjectedById(int id);
}
