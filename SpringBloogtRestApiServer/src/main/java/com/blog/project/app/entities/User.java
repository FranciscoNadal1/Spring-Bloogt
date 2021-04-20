package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Post.PostByUser;
import com.blog.project.app.entities.chat.Chat;

@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String username;

	@NotEmpty
	private String name;
	
	@NotEmpty
	private String surname;

	@NotEmpty
	private String email;

	@NotEmpty
	private String avatar;

	@NotEmpty
	private String password;


	@Basic(optional=true)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Role> roles;


	@ManyToMany
	@JoinTable(name = "user_following", joinColumns = @JoinColumn(name = "following_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> following;

	@ManyToMany(cascade = CascadeType.ALL)	
	private List<Chat> chats;


	
	@OneToMany
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private List<Comments> comments;

	@OneToMany
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private List<Post> posts;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getId() {
		return id;
	}

	public List<Post> getPosts() {

		Collections.sort(posts);
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date created_at) {
		this.createdAt = created_at;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public List<String> getUserRoles() {
		List<String> listString = new LinkedList<>();
		
		for(Role role: roles) {
			listString.add(role.getAuthority());
		}
		
		return listString;
	}
	

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Comments> getComments() {
		Collections.sort(comments);
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}
	
	
	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}

	@Override
	public String toString() {
		
		
		return "|Username|"+ this.getUsername()+"|";
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields
	





	public interface UserData {
		String getId();
		
		String getUsername();
	    String getName();
		String getAvatar();
		String getSurname();
		String getEmail();
		
		List<String> getUserRoles();
		Date getCreatedAt();
		List<OnlyUsername> getFollowing();
	}



	
	public interface UserComments {
		String getId();
		String getUsername();
		List<ShowComments> getComments();
	}

	public interface UserPosts {
		String getId();
		String getUsername();
		List<PostByUser> getPosts();
	}
	
	public interface OnlyUsername {
		String getId();
		String getAvatar();
		String getUsername();
	}
	
	public interface UserFollowData extends OnlyUsername{
		List<OnlyUsername> getFollowing();
	}
}
