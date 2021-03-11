package com.blog.project.app.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private ICategoryService categoryService;

	private String contentType = "application/json";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		GET Methods
///////////
///////////		Methods to retrieve information

	@GetMapping("/getAllCategories")
	public List<CategoryList> getAllCategories(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<CategoryList> returningJSON = categoryService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}

	@GetMapping("/getCategoryById/{id}")
	public List<CategoryDetails> getCategoryById(HttpServletResponse response, HttpServletRequest request,
			@PathVariable(value = "id") int id) {
		response.setContentType(contentType);

		List<CategoryDetails> returningJSON = categoryService.findOne(id);

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////
///////////		Methods to create new information
	

	@PostMapping("/newCategory")
	public JSONObject createCategory(HttpServletResponse response, HttpServletRequest request, @RequestBody Map<String, Object> payload) {

		if(payload.isEmpty())
			throw new NoPayloadDataException();
		
		response.setContentType(contentType);


		
		Category newCategory = new Category();

		String categoryName = (String) payload.get("name");
		newCategory.setName(categoryName);

		categoryService.save(newCategory);

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "category created : " + categoryName);
		return responseJson;
	}
	
	
	
	
	
}
