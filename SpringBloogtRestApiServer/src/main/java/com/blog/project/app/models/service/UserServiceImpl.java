package com.blog.project.app.models.service;

import java.util.ArrayList;
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
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.dao.IUser;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private IUser userDao;
	
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
	public List<UserPosts> findAllPostsOfUserProjectedById(int id) {
		return (List<UserPosts>)userDao.findAllPostsOfUserProjectedById(id);
	}

	@Override
	public User findReturnUserById(int id) {
		return (User) userDao.findReturnUserById(id);
	}

///////////////////////////////////////////////////////////////////////////////////
	public User getLoggedUser() {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String loggedUserUsername = loggedInUser.getName();
		User loggedUser = this.getUserByUsername(loggedUserUsername);

		return loggedUser;
	}
	
	@Override
	public List<User> getUsersThatFollowUser(String username) {
		User user = userDao.findUserByUsername(username);
		List<User> userList = userDao.findFollowingById(user.getId());
		
		return userList;
	}
	
	@Transactional
	public void followUser(String username) {
		User userThatFollows = this.getLoggedUser();
		User userThatIsFollowed = this.getUserByUsername(username);

		List<User> followedBy = this.getUsersThatFollowUser(username);
		
		for(User user : followedBy) 
			if(user.getUsername().equals(userThatFollows.getUsername())) 
				return;				
				
		userThatFollows.getFollowing().add(userThatIsFollowed);
		userDao.save(userThatFollows);
		
	}

}
