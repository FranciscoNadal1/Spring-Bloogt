package com.blog.project.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Category.CategoryNumberOfPosts;
import com.blog.project.app.models.dao.ICategory;

@Service
public class CategoryServiceImpl implements ICategoryService {

	@Autowired
	private ICategory categoryDao;

	@Override
	public List<Category> findAll() {
		return (List<Category>) categoryDao.findAll();
	}

	@Override
	public void save(Category category) {
		categoryDao.save(category);
	}

	@Override
	public List<CategoryDetails> findOne(int id) {
		return (List<CategoryDetails>) categoryDao.findById(id);
	}

	@Override
	public void deleteById(int id) {
		categoryDao.deleteById(id);

	}


//////////////////////////////////////////////	
// CUSTOM

	@Override
	public List<CategoryList> findAllProjectedBy() {
		return (List<CategoryList>) categoryDao.findAllProjectedBy();
	}

	@Override
	public List<CategoryDetails> findPostsOfCategoryById(int id) {
		return (List<CategoryDetails>) categoryDao.findPostsOfCategoryById(id);
	}

	@Override
	public List<CategoryDetails> findAllCategoriesAndPostBy() {
		return (List<CategoryDetails>) categoryDao.findAllCategoriesAndPostBy();
	}

	@Override
	public Category findCategoryById(int id) {
		return (Category) categoryDao.findCategoryById(id);
	}

	@Override
	public CategoryNumberOfPosts findAllnumberOfPostsById(int id) {
		// TODO Auto-generated method stub
		return (CategoryNumberOfPosts) categoryDao.findAllnumberOfPostsById(id);
	}
	
	
}
