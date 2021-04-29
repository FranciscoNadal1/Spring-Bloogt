package com.blog.project.app.entities.reaction;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.User;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Reaction {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private boolean reaction;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User reactedBy;


	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;
	

	////////////////////////////////


	
	public Reaction() {
		super();
	}




	public Date getCreatedAt() {
		return createdAt;
	}




	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}




	public boolean isReaction() {
		return reaction;
	}




	public void setReaction(boolean reaction) {
		this.reaction = reaction;
	}

	public boolean getReaction() {
		return this.reaction;
	}


	public User getReactedBy() {
		return reactedBy;
	}

	public void setReactedBy(User reactedBy) {
		this.reactedBy = reactedBy;
	}

	public int getId() {
		return id;
	}
	
	
}
