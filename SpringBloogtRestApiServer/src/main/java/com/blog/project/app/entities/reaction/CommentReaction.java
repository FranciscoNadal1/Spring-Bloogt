package com.blog.project.app.entities.reaction;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Comments;

@Entity
public class CommentReaction extends Reaction {

	@ManyToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "reaction_id", referencedColumnName = "id")
	private Comments comment;

	public Comments getComment() {
		return comment;
	}

	public void setComment(Comments comment) {
		this.comment = comment;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface ReactionCommentByUser {

		@Value("#{target.getPostId()}")
		int getPostId();

		Date getCreatedAt();

		boolean getReaction();
	}
}
