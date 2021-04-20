package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserFollowData;
import com.blog.project.app.entities.User.UserPosts;

public interface IUser extends BaseRepository <User, Long> {

	
	User findUserForLoginByUsername(String username);
	
	List<UserData> findByEmail(String email);
	UserData findByUsername(String username);
	UserFollowData findAllFollowDataByUsername(String username);
	List<UserData> findById(int id);
	Iterable<User> findAll();
	List<UserData> findAllProjectedBy();


	
    User findAuthenticationByUsername(@Param("username") String username);	
    User findUserByUsername(String username);	
	User findReturnUserById(int id);
	List<UserComments> findAllCommentsOfUserProjectedById(int id);
	List<UserPosts> findAllPostsOfUserProjectedById(int id);


	@Query(value = "SELECT user.* FROM user_following, user where user_following.following_id = user.id and user_following.user_id = ?1", nativeQuery = true)					
	List<User> findFollowingById(int userid);
	
	@Query(value = "SELECT user.* FROM user_following, user where user_following.following_id = user.id and user_following.user_id = ?1", nativeQuery = true)					
	List<OnlyUsername> findFollowerDataById(int userid);
	

	@Query(value = "SELECT user.* FROM user_following, user where user_following.following_id = user.id and user_following.following_id = ?1", nativeQuery = true)					
	List<OnlyUsername> findFollowedDataById(int userid);
}
