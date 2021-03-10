package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserData;

public interface IUser extends BaseRepository <User, Long> {

	List<UserData> findByEmail(String email);
	List<UserData> findByUsername(String username);
	List<UserData> findById(int id);
	Iterable<User> findAll();
	List<UserData> findAllProjectedBy();
	
}
