package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Comments.ShowAllComments;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;

public interface IComments extends BaseRepository <Comments, Long> {



	Iterable<Comments> findAll();
	List<ShowAllComments> findById(int id);
	List<ShowAllComments> findAllProjectedBy();
	

}
