package com.blog.project.app.entities.reaction;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.blog.project.app.entities.Post;

@Entity
public class PostReaction extends Reaction{

	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Post post;
	
	
	
	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post2) {
		this.post = post2;
	}

}
