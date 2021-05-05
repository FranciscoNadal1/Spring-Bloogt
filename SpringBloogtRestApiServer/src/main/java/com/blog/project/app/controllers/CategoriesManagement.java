package com.blog.project.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.service.ICategoryService;

//@RequestMapping("/website")
@Controller 
public class CategoriesManagement {

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private static ICategoryService staticCategoryService;

	
	public static List<CategoryList> staticCategoryList(){
		
		List<CategoryList> returningJSON = staticCategoryService.findAllProjectedBy();
		System.out.println();
		return returningJSON;		
	}
	
}
