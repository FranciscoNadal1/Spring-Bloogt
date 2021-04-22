package com.blog.project.app.rest.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Category.CategoryNumberOfPosts;
import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.rest.auth.JWTHandler;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private JWTHandler jwtHandler;
	
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

	@GetMapping("/getAllPostsByCategoryId")
	public List<CategoryDetails> getAllPostsByCategoryId(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<CategoryDetails> returningJSON = categoryService.findAllCategoriesAndPostBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		POST Methods
///////////
///////////		Methods to create new information

	@PostMapping("/newCategory")
	public JSONObject createCategory(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> payload, @RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_MODERATOR", "ROLE_ADMIN"))
			throw new UnauthorizedArea();
		
		if (payload.isEmpty())
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

// TODO	Must securize PUT and DELETE METHODS 

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		PUT Methods
///////////
///////////		Methods to update information

	@PutMapping("/updateCategory/{id}")
	public JSONObject updateCategoryById(HttpServletResponse response, HttpServletRequest request,
			@RequestBody Map<String, Object> payload, @PathVariable(value = "id") int id, @RequestHeader(value="Authorization", required=false) String authorization) {

		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_MODERATOR", "ROLE_ADMIN"))
			throw new UnauthorizedArea();
		
		if (payload.isEmpty())
			throw new NoPayloadDataException();

		response.setContentType(contentType);

		Category newCategory = categoryService.findCategoryById(id);
		String categoryPreviousName = newCategory.getName();

		String categoryName = (String) payload.get("name");
		newCategory.setName(categoryName);

		categoryService.save(newCategory);

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message",
				"Category " + categoryPreviousName + " is now updated. New name is : " + categoryName);

		responseJson.appendField("oldName", categoryPreviousName);
		responseJson.appendField("newName", categoryName);
		return responseJson;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////		DELETE Methods
///////////
///////////		Methods to update information

	@DeleteMapping("/deleteCategory/{id}")
	@Transactional
	public JSONObject deleteCategoryById(HttpServletResponse response, HttpServletRequest request, @PathVariable(value = "id") int id, @RequestHeader(value="Authorization", required=false) String authorization) {
		
		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

		CategoryNumberOfPosts returningJSON = categoryService.findAllnumberOfPostsById(id);
		if (returningJSON.getPostCount() != 0)
			throw new RuntimeException("Can't delete a category that has related data");

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "eliminated");

		return responseJson;

	}
}
