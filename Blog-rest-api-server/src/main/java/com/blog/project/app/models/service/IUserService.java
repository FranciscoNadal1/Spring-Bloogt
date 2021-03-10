package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserData;

public interface IUserService {
/// Default
	public List<User> findAll();

	public void save(User user);

	public User findOne(Long id);

	public void delete(Long id);
	
/// Custom

	public List<UserData> findByEmail(String email);
	public List<UserData> findByUsername(String username);
}
