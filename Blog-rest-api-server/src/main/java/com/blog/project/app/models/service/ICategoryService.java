package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Category.CategoryNumberOfPosts;

public interface ICategoryService {
/// Default
	public List<Category> findAll();

	public void save(Category category);



/// Custom

	public void deleteById(int id);
	public Category findCategoryById(int id);
	public List<CategoryDetails> findOne(int id);
	public List<CategoryList> findAllProjectedBy();


	public List<CategoryDetails> findAllCategoriesAndPostBy();
	public List<CategoryDetails> findPostsOfCategoryById(int id);
	public CategoryNumberOfPosts findAllnumberOfPostsById(int id);
}
