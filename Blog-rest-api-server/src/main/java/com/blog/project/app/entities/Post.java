package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

@Entity
public class Post  implements Serializable  {

	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	

	@NotEmpty
	private String title;
	
	@NotEmpty
	private String created_at;
	
	@NotEmpty
	private String content;
	
	
	@ManyToMany
	@JoinTable(
			  name = "Hashtag_post", 
			  joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), 
			  inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
	private List<HashTag> hashtags;
	
	
	@OneToMany
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private List<Comments> comments;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User createdBy;	

	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
}
