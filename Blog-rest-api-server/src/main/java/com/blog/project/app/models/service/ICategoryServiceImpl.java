package com.blog.project.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.models.dao.ICategory;

@Service
public class ICategoryServiceImpl implements ICategoryService {

	@Autowired
	private ICategory categoryDao;

	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
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
	public void delete(Long id) {
		categoryDao.deleteById(id);

	}

	@Override
	public List<CategoryList> findAllProjectedBy() {
		return (List<CategoryList>) categoryDao.findAllProjectedBy();
	}

//////////////////////////////////////////////	
// CUSTOM

}
