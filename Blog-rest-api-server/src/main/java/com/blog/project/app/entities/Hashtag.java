package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Value;

import com.blog.project.app.entities.Post.PostDetails;

@Entity
public class Hashtag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	@Column(unique = true)
	private String name;

	@ManyToMany
	@JoinTable(name = "Hashtag_post", joinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
	private List<Post> posts;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Hashtag() {

	}
	public Hashtag(Hashtag hashtag) {
		this.name = hashtag.getName();

	}	
	public Hashtag(@NotEmpty String name, List<Post> posts) {
		this.name = name;
		this.posts = posts;
	}
	public Hashtag(@NotEmpty String name, Post post) {
		List <Post> posts = new ArrayList<Post>();
		posts.add(post);
		this.name = name;
		
		this.posts = posts;
	}	
	
	
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

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface HashtagShow {

		String getId();
		String getName();
		@Value("#{target.getPosts().size()}")
	    int getPostsCount();
	}
	public interface PostsOfHashtag {

		String getId();
		String getName();
		@Value("#{target.getPosts().size()}")
	    int getPostsCount();
		List<PostDetails> getPosts();
	}
}
