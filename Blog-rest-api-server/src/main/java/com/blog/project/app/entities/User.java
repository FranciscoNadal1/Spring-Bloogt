package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

@Entity
public class User implements Serializable  {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String username;

	@NotEmpty
	private String surname;
	
	@NotEmpty
	private String avatar;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	private String created_at;
	
	@NotEmpty
	private String role;
	
	/*
	@OneToMany
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private List<Post> created_posts;
	*/
	

	@OneToMany
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private List<Comments> comments;
	
}
