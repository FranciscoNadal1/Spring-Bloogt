package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Comments.ShowAllComments;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;

public interface IComments extends BaseRepository <Comments, Long> {


	Comments findCommentsById(int id);

	Iterable<Comments> findAll();
	List<ShowAllComments> findById(int id);
	List<ShowComments> findByPost(Post post);
	List<ShowAllComments> findAllProjectedBy();

	List<Comments> findByCreatedBy(User user);
	public Comments findCommentById(int id);

}
