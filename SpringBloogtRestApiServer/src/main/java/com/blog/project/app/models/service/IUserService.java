package com.blog.project.app.models.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserFollowData;
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
	public UserFollowData getUserFollowDataByUsername(String username);
	
	public UserData getUserDataByUsername(String username);

	public List<UserData> findAllProjectedBy();
	public void saveUserAndAssignRole(User user, String role);


	public User findReturnUserById(int id);
	
	
	//User Comments
	public List<UserComments> findAllCommentsOfUserProjectedById(int id);

	//User Posts
	public List<UserPosts> findAllPostsOfUserProjectedById(int id);
	
	////////////////////////////////////////////////////////////////
	public User getLoggedUser();
	

	public List<User> getUsersThatFollowUser(String username);
	public List<OnlyUsername> getProjectionUsersThatFollowUser(String username);
	public List<OnlyUsername> getProjectionUsersThatAreFollowedByUser(String username);

	public void unfollowUserCommon(User userThatFollows, String username);
	public void followUserCommon(User userThatFollows, String username);
	public void followUser(String username);
}
