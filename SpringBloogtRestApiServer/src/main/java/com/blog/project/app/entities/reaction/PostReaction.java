package com.blog.project.app.entities.reaction;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Post;

@Entity
public class PostReaction extends Reaction {

	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Post post;

	public Post getPost() {
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
		Date getCreatedAt();
		boolean getReaction();
	}
	
}
