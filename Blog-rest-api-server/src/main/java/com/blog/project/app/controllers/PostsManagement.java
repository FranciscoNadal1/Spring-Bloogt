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
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateAsc;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateDesc;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IHashtagService;
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

	@Autowired
	private IHashtagService hashtagService;

	/*
	 * @Autowired private GeneralRepository generalRepo;
	 */
	@GetMapping("/")
	public String index(Map<String, Object> model) {
		return listPost(model);
	}

	@GetMapping("/list")
	public String listPost(Map<String, Object> model) {
		model.put("titulo", "Latest posts");

		List<showPosts> returningJSON = postService.findAllPostsProjection();
		addDataToMenu(model);

		model.put("posts", returningJSON);

		return "allposts";
	}

	@GetMapping("/list/{id}")
	public String listPostsById(Map<String, Object> model, @PathVariable(value = "id") int id) {

		// TODO Must have an input to choose the prefered order
		String orderType;
		if (model.get("sort") != null)
			orderType = (String) model.get("sort");
		else
			orderType = "default";

		postService.addVisit(id);
		
		
		
		PostDetails returningJSON;
		switch (orderType) {
		case "asc":
			returningJSON = (PostDetailsCommentsSortByDateDesc) postService.findPostByIdAndSortByCreatedDateDesc(id);
			break;
		case "best":
		case "worst":
		case "desc":
		default:
			returningJSON = (PostDetailsCommentsSortByDateAsc) postService.findPostByIdAndSortByCreatedDateAsc(id);
		}

		model.put("titulo", returningJSON.getTitle());

		addDataToMenu(model);
		model.put("post", returningJSON);

		return "postdetails";
	}

	@GetMapping("/list/{id}/sortComments/{sort}")
	public String returnSortedPostList(Map<String, Object> model, @PathVariable(value = "id") int id,
			@PathVariable(value = "sort") String sort) {
		model.put("sort", sort);
		return listPostsById(model, id);
	}

	@GetMapping("/fromCategory/{id}")
	public String listPostsFromCategory(Map<String, Object> model, @PathVariable(value = "id") int id) {

		List<CategoryDetails> returningJSON = categoryService.findPostsOfCategoryById(id);

		List<showPosts> returningPostJSON = returningJSON.get(0).getPosts();

		model.put("titulo", returningJSON.get(0).getName());

		addDataToMenu(model);

		model.put("posts", returningPostJSON);

		return "allposts";
	}

	@GetMapping("/fromHashtag/{hashtag}")
	public String listPostsFromHashtag(Map<String, Object> model, @PathVariable(value = "hashtag") String hashtag) {

		PostsOfHashtag hash = hashtagService.findPostOfHashtagByName(hashtag);

		List<PostDetails> returningPostJSON = hash.getPosts();

		model.put("titulo", "#" + hashtag);

		addDataToMenu(model);
		
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
		if (getPostId == null)
			throw new RuntimeException();

		String commentContent = request.getParameter("content");

// TODO User must be the one that is logged in
		Comments newComment = new Comments(commentContent, userService.findReturnUserById(1),
				postService.findReturnPostById(Integer.parseInt(getPostId)));

		commentService.save(newComment);

		return "redirect:" + request.getHeader("Referer");
	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////
	public Map<String, Object> addDataToMenu(Map<String, Object> model){

		model.put("categoriesForMenu", categoryService.findAllProjectedBy());
		model.put("hashtagsForMenu", hashtagService.findAllProjectedBy());
		
		return model;
		
	}
}
