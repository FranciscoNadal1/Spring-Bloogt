package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;

public interface ICategoryService {
/// Default
	public List<Category> findAll();

	public void save(Category category);

	public void delete(Long id);


/// Custom
	public List<CategoryDetails> findOne(int id);
	public List<CategoryList> findAllProjectedBy();


}
