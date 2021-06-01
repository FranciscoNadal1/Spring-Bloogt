package com.blog.project.app.entities.reaction;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.showPosts;

@Entity
public class PostReaction extends Reaction {

	@OneToOne()
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Post post;

	public Post getPosts() {
		return post;
	}

	public int getPostId() {
		return post.getId();
	}


	public void setPost(Post post2) {
		this.post = post2;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface ReactionPostByUser {

		@Value("#{target.getPostId()}")
		int getPostId();
		
		// TODO The post shouldn't be a child
		showPosts getPosts();

		//int this.getPosts().getId();
		
		Date getCreatedAt();
		boolean getReaction();
	}
	
}
