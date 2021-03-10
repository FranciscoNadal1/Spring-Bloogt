package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

@Entity
public class Category  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty
	private String name;
	
	@ManyToMany
	@JoinTable(
			  name = "category_hashtag", 
			  joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), 
			  inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
	private List<HashTag> hashtags;

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<HashTag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<HashTag> hashtags) {
		this.hashtags = hashtags;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
