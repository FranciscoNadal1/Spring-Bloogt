package com.blog.project.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;

//@RequestMapping("/website")
@RequestMapping("/post")
@Controller
public class PostsManagement {

	@Autowired
	private IPostService postService;

	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private ICommentsService commentService;

	@GetMapping("/")
	public String index(Map<String, Object> model) {
		return listPost(model);
	}

	@GetMapping("/list")
	public String listPost(Map<String, Object> model) {
		model.put("titulo", "Blog");

		List<showPosts> returningJSON = postService.findAllProjectedBy();
		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();
		model.put("categoriesForMenu", categoriesForMenu);

		model.put("posts", returningJSON);

		return "allposts";
	}

	@GetMapping("/list/{id}")
	public String listPostsById(Map<String, Object> model, @PathVariable(value = "id") int id) {

		List<PostDetails> returningJSON = postService.findOne(id);
		model.put("titulo", returningJSON.get(0).getTitle());

		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();
		model.put("categoriesForMenu", categoriesForMenu);
		
		model.put("posts", returningJSON);

		return "postdetails";
	}

	@GetMapping("/fromCategory/{id}")
	public String listPostsFromCategory(Map<String, Object> model, @PathVariable(value = "id") int id) {

		List<CategoryDetails> returningJSON = categoryService.findPostsOfCategoryById(id);
		
		
		
		List<showPosts> returningPostJSON = returningJSON.get(0).getPosts();
		
		model.put("titulo", returningJSON.get(0).getName());

		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();
		model.put("categoriesForMenu", categoriesForMenu);
		
		model.put("posts", returningPostJSON);

		return "allposts";
	}	
//////////////////////////////////////////////////////////////////////////////////
	
	@PostMapping("/sendComment")
	public String newComment(HttpServletRequest request, Map<String, Object> model) {
		System.out.println();

		String getPostId = null;
		StringTokenizer tokenizer = new StringTokenizer(request.getHeader("Referer"), "//");
	    while (tokenizer.hasMoreElements()) {
	    	getPostId = tokenizer.nextToken();
	    }
		if(getPostId == null)
			throw new RuntimeException();
			
		String commentContent = request.getParameter("content");
		
// TODO User must be the one that is logged in
		Comments newComment = new Comments(commentContent, userService.findReturnUserById(1),
				postService.findReturnPostById(Integer.parseInt(getPostId)));

		commentService.save(newComment);

		return "redirect:" + request.getHeader("Referer");
	}
}
