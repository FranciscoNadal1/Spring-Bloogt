package com.blog.project.app.entities.reaction;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.blog.project.app.entities.Comments;

@Entity
public class CommentReaction extends Reaction{
	
	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Comments comment;

	
	
	
	public Comments getComment() {
		return comment;
	}

	public void setComment(Comments comment) {
		this.comment = comment;
	}
	
	
}
