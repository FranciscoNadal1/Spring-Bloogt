package com.blog.project.app.entities.reaction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.blog.project.app.entities.Comments;

@Entity
public class CommentReaction extends Reaction{
	
	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Comments comment;

	
	
	
	public Comments getComment() {
		return comment;
	}

	public void setComment(Comments comment) {
		this.comment = comment;
	}
	
	
}
