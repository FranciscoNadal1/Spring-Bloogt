package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;

public interface IUser extends BaseRepository <User, Long> {

	
	User findUserForLoginByUsername(String username);
	
	List<UserData> findByEmail(String email);
	UserData findByUsername(String username);
	List<UserData> findById(int id);
	Iterable<User> findAll();
	List<UserData> findAllProjectedBy();


	
    User findAuthenticationByUsername(@Param("username") String username);	
	User findReturnUserById(int id);
	List<UserComments> findAllCommentsOfUserProjectedById(int id);
	List<UserPosts> findAllPostsOfUserProjectedById(int id);
}
