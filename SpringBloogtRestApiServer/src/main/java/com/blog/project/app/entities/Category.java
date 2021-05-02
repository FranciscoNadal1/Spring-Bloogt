package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.Post.PostNoContent;
import com.blog.project.app.entities.Post.showPosts;

@Entity
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String name;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "category_hashtag", joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
	private List<Hashtag> hashtags;

	@OneToMany
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private List<Post> posts;
	
	
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

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	public List<Post> getPosts() {
		Collections.reverse(this.posts);
		return posts;
	}
	
	public Post getLastPost() {
		List<Post> posts = this.getPosts();
		Collections.reverse(posts);
		for(Post post : posts)
			return post;
		return null;
	}
	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields


	public interface CategoryName {
		String getId();		
		String getName();
	}
	public interface CategoryList {
		String getId();		
		String getName();

		@Value("#{target.getPosts().size()}")
	    int getPostCount();

		@Value("#{target.getLastPost()}")
		PostNoContent getLastPost();

	}
	public interface CategoryDetails extends CategoryList{

		List<showPosts> getPosts();
		List<HashtagShow> getHashtags();
	}

	public interface CategoryNumberOfPosts {
		@Value("#{target.getPosts().size()}")
	    int getPostCount();
	}
}
