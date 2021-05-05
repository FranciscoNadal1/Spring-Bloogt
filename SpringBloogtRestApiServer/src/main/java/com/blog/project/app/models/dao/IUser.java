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

	@Query("select count(*) from User")
	int countAll();

	
	
	
	@Query(value = "SELECT user.* 	FROM post, user	where post.user_id = user.id and user.username like 'BOT-%'	group by user.username", nativeQuery = true)	
	List<User> getAllBotsThatHavePosts();
	
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
	UserComments findAllCommentsOfUserProjectedByUsername(String username);
	UserPosts findAllPostsOfUserProjectedById(int id);
	UserPosts findAllPostsOfUserProjectedByUsername(String username);


	@Query(value = "SELECT user.* FROM user_following, user where user_following.following_id = user.id and user_following.user_id = ?1", nativeQuery = true)					
	List<User> findFollowingById(int userid);
	
	@Query(value = "SELECT user.* FROM user_following, user where user_following.following_id = user.id and user_following.user_id = ?1", nativeQuery = true)					
	List<OnlyUsername> findFollowerDataById(int userid);
	
//	select * from user	where user.id in(	SELECT user_following.user_id	FROM user_following, user 	where 	user_following.following_id = user.id 	and user.id = ?1)
	@Query(value = "select * from user	where user.id in(	SELECT user_following.user_id	FROM user_following, user 	where 	user_following.following_id = user.id 	and user.id = ?1)", nativeQuery = true)					
	List<OnlyUsername> findFollowedDataById(int userid);
}
