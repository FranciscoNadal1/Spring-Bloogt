package com.blog.project.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.dao.IUser;

@Service
public class IUserServiceImpl implements IUserService {

	@Autowired
	private IUser userDao;
	
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
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
	
//////////////////////////////////////////////	
// CUSTOM
	

	@Override
	public List<UserData> findByEmail(String email) {
		return (List<UserData>) userDao.findByEmail(email);
	}

	@Override
	public List<UserData> findByUsername(String username) {
		return (List<UserData>) userDao.findByUsername(username);
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
}
