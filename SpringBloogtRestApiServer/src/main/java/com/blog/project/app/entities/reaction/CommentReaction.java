package com.blog.project.app.entities.reaction;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Comments.ShowComments;

@Entity
public class CommentReaction extends Reaction {

	@ManyToOne()
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Comments comment;

	public Comments getComments() {
		return comment;
	}

	public void setComment(Comments comment) {
		this.comment = comment;
	}
	
	public int getCommentId() {
		return this.comment.getId();
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface ReactionCommentByUser {

		@Value("#{target.getCommentId()}")
		int getCommentId();

		ShowComments getComments();

		Date getCreatedAt();

		boolean getReaction();
	}
}
