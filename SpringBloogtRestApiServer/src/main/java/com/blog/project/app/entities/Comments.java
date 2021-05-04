package com.blog.project.app.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Post.PostNoContent;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.reaction.CommentReaction;
import com.blog.project.app.entities.reaction.Reaction;
import com.blog.project.app.utils.LocalUtils;

@Entity
public class Comments  implements Comparable<Comments> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length=1000)
	private String message;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User createdBy;

	@ManyToOne()
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Post post;

//	@Temporal(TemporalType.DATE)
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
	private List<CommentReaction> reaction;
	
	
	private boolean removedByModerator;



	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Comments() {
	}
	
	public Comments(String message, User createdBy, Post post) {
		this.message = message;
		this.createdBy = createdBy;
		this.post = post;
		this.createdAt = LocalUtils.getActualDate();
		this.removedByModerator = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		if(this.isRemovedByModerator())
			return "This message was eliminated by a moderator";
		else
			return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	public Post getPost() {
		return post;
	}
	public int getPostId() {
		return this.getPost().getId();
	}
	
	public void setPost(Post post) {
		this.post = post;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public boolean isRemovedByModerator() {
		return removedByModerator;
	}

	public void setRemovedByModerator(boolean removedByModerator) {
		this.removedByModerator = removedByModerator;
	}
	
	public List<CommentReaction> getReaction() {
		return reaction;
	}

	public void setReaction(List<CommentReaction> reaction) {
		this.reaction = reaction;
	}

	public int getPositiveReactions() {
		int positiveReactions = 0;
		List<CommentReaction> listReaction = this.getReaction();
		for(Reaction reaction : listReaction) 
			if(reaction.getReaction() == true)
				positiveReactions++;
		
		return positiveReactions;
	}
	
	public int getNegativeReactions() {
		int negativeReactions = 0;
		List<CommentReaction> listReaction = this.getReaction();
		for(Reaction reaction : listReaction) 
			if(reaction.getReaction() == false)
				negativeReactions++;
		
		return negativeReactions;
	}	
	
	public int getTotalReactions() {
		int reactions = 0;
		List<CommentReaction> listReaction = this.getReaction();
		for(Reaction reaction : listReaction) 
				reactions++;
		
		return reactions;
	}		
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields
	




	public interface ShowComments extends Comparable<ShowComments> {

		String getId();		
		OnlyUsername getCreatedBy();
		String getMessage();		
		Date getCreatedAt();
		boolean isRemovedByModerator();
		//void setPost(Post post);
		//int getReaction();
		@Value("#{target.getNegativeReactions()}")
		int getNegativeReactions();
		@Value("#{target.getPositiveReactions()}")
		int getPositiveReactions();
		@Value("#{target.getTotalReactions()}")
		int getTotalReactions();

		@Value("#{target.getPostId()}")
		int getPostId();

	}

	public interface ShowAllComments extends ShowComments{

		//String getId();
		//OnlyUsername getCreatedBy();		
		//String getMessage();		
		PostNoContent getPost();
		//Date getCreatedAt();
		//boolean isRemovedByModerator();
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////		Comparators
///////////////
///////////////		Compare objects

    @Override public int compareTo(Comments u) { 

        if (getCreatedAt() == null || u.getCreatedAt() == null) { 
          return 0; 
        } 
        return getCreatedAt().compareTo(u.getCreatedAt());
      } 

}
