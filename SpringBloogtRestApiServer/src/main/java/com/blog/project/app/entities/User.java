package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.chat.Chat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotEmpty
	private String username;

	@NotEmpty
	private String name;
	
	@NotEmpty
	private String surname;

	@NotNull
	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	private String avatar;

	@NotEmpty
	private String background;

	@NotEmpty
	@NotNull
	private String password;

	@Size(min = 0, max = 500)
	@Basic(optional=true)
	private String bio;

	@Basic(optional=true)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Role> roles;

	@ManyToMany
	@JoinTable(name = "user_following", joinColumns = @JoinColumn(name = "following_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private List<User> following;

	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	private List<Chat> chats;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private List<Comments> comments;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private List<Post> posts;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Post> getPosts() {
		Collections.sort(posts);
		return posts;
	}
	/*
	public int getId() {
		return id;
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


	

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
*/
	public List<String> getUserRoles() {
		List<String> listString = new LinkedList<>();
		
		for(Role role: roles) {
			listString.add(role.getAuthority());
		}
		
		return listString;
	}
	
	public List<Comments> getComments() {
		Collections.sort(comments);
		return this.comments;
	}
/*
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
	*/
	public int writtenPosts() {
		return this.getPosts().size();
		
	}	
	public int writtenComments() {
		return this.getComments().size();
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
		String getBackground();
		String getSurname();
		String getEmail();
		String getBio();
		
		List<String> getUserRoles();
		Date getCreatedAt();
		List<OnlyUsername> getFollowing();

		@Value("#{target.writtenPosts()}")
		int getWrittenPosts();
		
		@Value("#{target.writtenComments()}")
		int getWrittenComments();
	}



	
	public interface UserComments {
		String getId();
		String getUsername();

		@Value("#{target.getComments()}")
		List<ShowComments> getComments();
	}

	public interface UserPosts {
		String getId();
		String getUsername();
		List<showPosts> getPosts();
	}
	
	public interface OnlyUsername {
		String getId();
		String getAvatar();
		String getBackground();
		String getUsername();
		String getName();
		String getSurname();
	}
	
	public interface UserFollowData extends OnlyUsername{
		List<OnlyUsername> getFollowing();
	}
}
