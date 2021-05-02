package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Category.CategoryName;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.reaction.CommentReaction;
import com.blog.project.app.entities.reaction.PostReaction;
import com.blog.project.app.entities.reaction.Reaction;

@Entity
public class Post implements Serializable, Comparable<Post> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String title;

	private String imagePost;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "created_at")
	private Date createdAt;

	@Lob
	@Column(length = 8096)
	@NotEmpty
	private String content;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Hashtag_post", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
	private List<Hashtag> hashtags;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private List<Comments> comments;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	// TODO This should be on a different table, to register the date of the view to get statistics, but user support comes first.
	@Column(name = "times_viewed")
	private int timesViewed;
	
	

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	private List<PostReaction> reaction;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date created_at) {
		this.createdAt = created_at;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImagePost() {
		return imagePost;
	}

	public void setImagePost(String imagePost) {
		this.imagePost = imagePost;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}

	public void sortCommentsByDate() {
		Collections.sort(comments);
	}

	public List<Comments> getComments() {
		Collections.sort(comments);
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public int getTimesViewed() {
		return timesViewed;
	}

	public void setTimesViewed(int timesViewed) {
		this.timesViewed = timesViewed;
	}
	
	public List<PostReaction> getReaction() {
		return reaction;
	}

	public void setReaction(List<PostReaction> reaction) {
		this.reaction = reaction;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Custom methods
///////////
///////////		Custom methods for the post class	
	
	public void addVisit() {
		this.timesViewed = this.timesViewed+1;
	}

	public List<Comments> getCommentsSortDateAsc() {
		Collections.sort(comments);
		return comments;
	}

	public List<Comments> getCommentsSortDateDesc() {
		Collections.reverse(comments);
		return comments;
	}
	
	public int getPositiveReactions() {
		int positiveReactions = 0;
		List<PostReaction> listReaction = this.getReaction();
		for(Reaction reaction : listReaction) 
			if(reaction.getReaction() == true)
				positiveReactions++;
		
		return positiveReactions;
	}
	
	public int getNegativeReactions() {
		int negativeReactions = 0;
		List<PostReaction> listReaction = this.getReaction();
		for(Reaction reaction : listReaction) 
			if(reaction.getReaction() == false)
				negativeReactions++;
		
		return negativeReactions;
	}	
	
	public int getTotalReactions() {
		int reactions = 0;
		List<PostReaction> listReaction = this.getReaction();
		for(Reaction reaction : listReaction) 
				reactions++;
		
		return reactions;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		Projections
///////////
///////////		Used to avoid showing all fields

	public interface showPosts {

		String getId();
		String getTitle();
		String getContent();
		String getCreatedAt();
		List<HashtagShow> getHashtags();
		OnlyUsername getCreatedBy();
		String getImagePost();

		@Value("#{target.getComments().size()}")
		int getCommentaryCount();

		CategoryName getCategory();		
		int getTimesViewed();
		

		@Value("#{target.getNegativeReactions()}")
		int getNegativeReactions();
		@Value("#{target.getPositiveReactions()}")
		int getPositiveReactions();
		@Value("#{target.getTotalReactions()}")
		int getTotalReactions();


	}

	public interface PostDetails extends showPosts{

	}

	// It will load PostDetails and then add the sorted comments
	public interface PostDetailsCommentsSortByDateAsc extends PostDetails {
		@Value("#{target.getCommentsSortDateAsc()}")
		List<ShowComments> getComments();
	}

	// It will load PostDetails and then add the sorted comments
	public interface PostDetailsCommentsSortByDateDesc extends PostDetails {
		@Value("#{target.getCommentsSortDateDesc()}")
		List<ShowComments> getComments();
	}

	public interface PostDetailsCommentsSortByThumbsUp extends PostDetails {
		// TODO Thumbs up are not implemented !
	}
	public interface PostBy{

		String getId();
		String getTitle();
		String getContent();
		Date getCreatedAt();
		@Value("#{target.getComments().size()}")
		int getCommentaryCount();
		CategoryName getCategory();
		String getImagePost();
		int getTimesViewed();
		

		@Value("#{target.getNegativeReactions()}")
		int getNegativeReactions();
		@Value("#{target.getPositiveReactions()}")
		int getPositiveReactions();
		@Value("#{target.getTotalReactions()}")
		int getTotalReactions();
	}
	public interface PostByUser extends PostBy {
		List<HashtagShow> getHashtags();
	}
	public interface PostNoContent{
		String getId();
		String getTitle();
		OnlyUsername getCreatedBy();
		Date getCreatedAt();
		

		@Value("#{target.getNegativeReactions()}")
		int getNegativeReactions();
		@Value("#{target.getPositiveReactions()}")
		int getPositiveReactions();
		@Value("#{target.getTotalReactions()}")
		int getTotalReactions();
	}
	
	public interface PostByHashtag  extends PostBy{


	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////		Comparators
///////////////
///////////////		Compare objects

	@Override
	public int compareTo(Post u) {

		if (getCreatedAt() == null || u.getCreatedAt() == null) {
			return 0;
		}
		return getCreatedAt().compareTo(u.getCreatedAt());
	}

}
