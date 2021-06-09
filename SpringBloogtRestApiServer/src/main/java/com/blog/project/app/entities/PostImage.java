package com.blog.project.app.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
public class PostImage {


	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;
	
	@NotNull
	@NotEmpty
    private String imagePost;

	@ManyToOne
	@JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

	public PostImage() {
		
	}	
	
	public PostImage(String imagePost, Post post) {
		this.imagePost = imagePost;
		this.post = post;
	}	
	
}
