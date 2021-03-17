package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Category.CategoryNumberOfPosts;

public interface ICategory extends BaseRepository <Category, Long> {



	Category findCategoryById(int id);
	List<Category> findAll();
	Category findCategoryByName(String str);

	void deleteById(int id);

	List<CategoryList> findAllProjectedBy();
	List<CategoryDetails> findById(int id);
	List<CategoryDetails> findAllCategoriesAndPostBy();
	List<CategoryDetails> findPostsOfCategoryById(int id);
	CategoryNumberOfPosts findAllnumberOfPostsById(int id);
}
