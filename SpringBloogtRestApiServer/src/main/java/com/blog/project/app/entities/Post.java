package com.blog.project.app.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import com.blog.project.app.entities.Category.CategoryName;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.User.OnlyUsername;
import com.blog.project.app.entities.reaction.PostReaction;
import com.blog.project.app.entities.reaction.Reaction;

import lombok.Data;

@Entity
@Data
public class Post implements Serializable, Comparable<Post> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotEmpty
	private String title;
/*
	@ElementCollection
	private List<String> imagePost;
*/
	
//	Element collection had to be removed and placed on a dedicated entity, for compatiblity reasons with symfony
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "post_id", referencedColumnName = "id")	
	@Column(name = "post_image_post")
	private List<PostImage> imagePost;

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

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
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
	
	

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "post")
    @OnDelete(action = OnDeleteAction.CASCADE)
	private List<PostReaction> reaction;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, mappedBy = "relatedPost")
    @OnDelete(action = OnDeleteAction.CASCADE)
	private List<Notifications> notifications;
	
	////////////////	Only for shared Posts
	@Transient
	public boolean isShared;

	@Transient
	public OnlyUsername sharedBy;

	@Transient
	public Date sharedAt;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
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

	public List<String> getImagePost() {
		return imagePost;
	}

	public void setImagePost(List<String> imagePostList) {
		this.imagePost = imagePostList;
	}

	public List<Hashtag> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<Hashtag> hashtags) {
		this.hashtags = hashtags;
	}
*/
	public void sortCommentsByDate() {
		Collections.sort(comments);
	}
/*
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
	*/
	public List<PostReaction> getReaction() {
		return reaction;
	}

	public void setReaction(List<PostReaction> reaction) {
		this.reaction = reaction;
	}
	
	
	
	public List<String> getImagePost() {
		List<String> listString = new LinkedList<>();
		for (PostImage postImage: this.imagePost) {
			listString.add(postImage.getImagePost());
		}
		
		return listString;
	}
	public void setImagePost(List<String> listString) {

		//this.imagePost.clear();
		
		this.imagePost = new LinkedList<PostImage>();

		for (String string: listString) {
			this.imagePost.add(new PostImage(string,this));
			
		}
		
		
		
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
		//return this.getReaction().size();
		
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

	public interface showPosts  extends Comparable<showPosts> {

		String getId();
		String getTitle();
		String getContent();
		Date getCreatedAt();
		void setCreatedAt(Date date);
		
		List<HashtagShow> getHashtags();
		OnlyUsername getCreatedBy();
		@Value("#{target.getImagePost()}")
		List<String> getImagePost();

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

/////////////////////// Only for shared Posts
		
		boolean getIsShared();
		void setIsShared(boolean bool);

		OnlyUsername getSharedBy();
		void setSharedBy(OnlyUsername user);		

		Date getSharedAt();
		void setSharedAt(Date date);
		
	}

	
	
	
	
	

	// It will load PostDetails and then add the sorted comments
	public interface PostDetailsCommentsSortByDateAsc extends showPosts {
		@Value("#{target.getCommentsSortDateAsc()}")
		List<ShowComments> getComments();
	}

	// It will load PostDetails and then add the sorted comments
	public interface PostDetailsCommentsSortByDateDesc extends showPosts {
		@Value("#{target.getCommentsSortDateDesc()}")
		List<ShowComments> getComments();
	}

	public interface PostDetailsCommentsSortByThumbsUp extends showPosts {
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
		@Value("#{target.getImagePost()}")
		List<String> getImagePost();
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

	public static class PostSortingComparator implements Comparator<showPosts> {

	    @Override
		public int compare(showPosts a, showPosts u) {

			if(a.getSharedAt() != null && u.getSharedAt() != null) {
				return a.getSharedAt().compareTo(u.getSharedAt());
			}	
			
			if(a.getSharedAt() != null && u.getSharedAt() == null) {
				return a.getSharedAt().compareTo(u.getCreatedAt());
			}	
			if(a.getSharedAt() == null && u.getSharedAt() != null) {
				return a.getCreatedAt().compareTo(u.getSharedAt());
			}			
			
			if (a.getCreatedAt() == null || u.getCreatedAt() == null) {
				return 0;
			}
			return a.getCreatedAt().compareTo(u.getCreatedAt());
		}
/*
		@Override
		public int compare(showPosts o1, showPosts o2) {
			// TODO Auto-generated method stub
			return 0;
		}
		*/


	}

	
	
	@Override
	public int compareTo(Post u) {

		if(getSharedAt() != null && u.getSharedAt() != null) {
			System.out.println("getsharedat not null both");
			return getSharedAt().compareTo(u.getSharedAt());
		}	
		
		if(getSharedAt() != null && u.getSharedAt() == null) {
			System.out.println("getsharedat not null first");
			return getSharedAt().compareTo(u.getCreatedAt());
		}	
		if(getSharedAt() == null && u.getSharedAt() != null) {
			System.out.println("getsharedat  null first not second");
			return getCreatedAt().compareTo(u.getSharedAt());
		}			
		
		
		
		
		
		
		if (getCreatedAt() == null || u.getCreatedAt() == null) {
			return 0;
		}
		return getCreatedAt().compareTo(u.getCreatedAt());
	}
	
	public boolean equals(Post post) {

		if(post.id == id && post.content == content)
			return true;
		else
			return false;
	}
	


}
