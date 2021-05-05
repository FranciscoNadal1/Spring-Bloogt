package com.blog.project.app.models.service.implementation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.project.app.entities.Role;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserFollowData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.dao.IRoles;
import com.blog.project.app.models.dao.IUser;
import com.blog.project.app.models.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private IUser userDao;
	
	@Autowired
	private IRoles rolesDao;
	
	@Override
	public User getUserByUsername(String username) {
		return userDao.findUserForLoginByUsername(username);
	}
	@Override
	public List<User> findAll() {
		return (List<User>) userDao.findAll();
	}

	@Override
	public void save(User user) {
		userDao.save(user);
	}

	@Override
	public List<UserData>  findOne(int id) {
		return (List<UserData>)userDao.findById(id);
	}

	@Override
	public void delete(Long id) {
		userDao.deleteById(id);

	}
///////////////////////////////////////////////////////
///	Login
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
        User user = userDao.findUserForLoginByUsername(username);
        
        if(user == null) {
        	logger.error("Username : '" + username + "' is not found on the system");
        	throw new UsernameNotFoundException("Username: " + username + " doesn't exist!");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for(Role role: user.getRoles()) {
        	logger.info("Role: ".concat(role.getAuthority()));
        	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        
        if(authorities.isEmpty()) {
        	logger.error("Login error:'" + username + "' has no roles!");
        	throw new UsernameNotFoundException("Login error: '" + username + "' has no roles!");
        }


		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
	
//////////////////////////////////////////////	
// CUSTOM
	

	@Override
	public List<UserData> findByEmail(String email) {
		return (List<UserData>) userDao.findByEmail(email);
	}

	@Override
	public UserData getUserDataByUsername(String username) {
		return (UserData) userDao.findByUsername(username);
	}

	@Override
	public List<UserData> findAllProjectedBy() {
		return (List<UserData>) userDao.findAllProjectedBy();
	}

	@Override
	public List<UserComments> findAllCommentsOfUserProjectedById(int id) {
		return (List<UserComments>)userDao.findAllCommentsOfUserProjectedById(id);
	}

	@Override
	public UserComments findAllCommentsOfUserProjectedByUsername(String username) {
		return (UserComments)userDao.findAllCommentsOfUserProjectedByUsername(username);
	}

	@Override
	public UserPosts findAllPostsOfUserProjectedById(int id) {
		return (UserPosts)userDao.findAllPostsOfUserProjectedById(id);
	}
	@Override
	public UserPosts findAllPostsOfUserProjectedByUsername(String username) {
		return (UserPosts)userDao.findAllPostsOfUserProjectedByUsername(username);
	}
	@Override
	public User findReturnUserById(int id) {
		return (User) userDao.findReturnUserById(id);
	}

///////////////////////////////////////////////////////////////////////////////////
	public User getLoggedUser() {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String loggedUserUsername;
		try {
			loggedUserUsername = loggedInUser.getName();
		} catch (NullPointerException e) {
			throw new RuntimeException("There are problems with the user logged");
		}
		User loggedUser = this.getUserByUsername(loggedUserUsername);

		return loggedUser;
	}
	
	@Override
	public List<User> getUsersThatFollowUser(String username) {
		User user = userDao.findUserByUsername(username);
		List<User> userList = userDao.findFollowingById(user.getId());
		
		return userList;
	}
	
	@Override
	public List<OnlyUsername> getProjectionUsersThatFollowUser(String username) {
		User user = userDao.findUserByUsername(username);
		List<OnlyUsername> userList = userDao.findFollowerDataById(user.getId());
		
		return userList;
	}	
	@Override
	
	public List<OnlyUsername> getProjectionUsersThatAreFollowedByUser(String username) {
		User user = userDao.findUserByUsername(username);
		List<OnlyUsername> userList = userDao.findFollowedDataById(user.getId());
		
		return userList;
	}		
	
	
	public void unfollowUserCommon(User userThatFollows, String username) {
		
		User userThatIsFollowed = this.getUserByUsername(username);

		List<User> followedBy = this.getUsersThatFollowUser(username);
		/*
		for(User user : followedBy) 
			if(user.getUsername().equals(userThatFollows.getUsername())) 
				throw new RuntimeException("User already followed");				
				*/
		if(userThatFollows.getFollowing().contains(userThatIsFollowed))
			userThatFollows.getFollowing().remove(userThatIsFollowed);
		else
			throw new RuntimeException("You are not following this user");		
			
		userDao.save(userThatFollows);
		
	}
	
//	@Transactional
	public void followUserCommon(User userThatFollows, String username) {
		
		User userThatIsFollowed = this.getUserByUsername(username);

		List<User> followedBy = this.getUsersThatFollowUser(username);
		
		for(User user : followedBy) 
			if(user.getUsername().equals(userThatFollows.getUsername())) 
				throw new RuntimeException("User already followed");				
				
		userThatFollows.getFollowing().add(userThatIsFollowed);
		userDao.save(userThatFollows);
		
	}
	
	public void followUser(String username) { 
		this.followUserCommon(this.getLoggedUser(), username);
	}
	
	


	@Override
	public UserFollowData getUserFollowDataByUsername(String username) {
		return (UserFollowData) userDao.findAllFollowDataByUsername(username);
	}
	
	
	@Override
	@Transactional
	public void saveUserAndAssignRole(User user, String role) {
		
		Role basicRole = new Role();
		basicRole.setAuthority("ROLE_USER");
		rolesDao.save(basicRole);		
		
		if(user.getRoles() != null)
			for(Role roleIt : user.getRoles())
				if(!roleIt.getAuthority().equals("ROLE_USER")) {
					throw new RuntimeException("User already contains that role");
				}
		
		
		if(user.getRoles() == null)
			user.setRoles(new LinkedList<Role>());
		
		user.getRoles().add(basicRole);
				
		userDao.save(user);
		
	}


}
