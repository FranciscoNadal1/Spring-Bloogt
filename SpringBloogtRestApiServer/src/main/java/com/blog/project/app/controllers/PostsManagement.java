package com.blog.project.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateAsc;
import com.blog.project.app.entities.Post.PostDetailsCommentsSortByDateDesc;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IHashtagService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IReactionService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

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

	@Autowired
	private LocalUtils utils;

	@Autowired
	private IReactionService reactionService;

	@GetMapping("/")
	public String index(Model model) {
		return listPost(model);
	}

	@GetMapping(value={"/list","/"})
	public String listPost(Model model) {
		model.addAttribute("titulo", "Latest posts");

		List<showPosts> returningJSON = postService.findAllPostsProjection();

		utils.addDataToMenu(model, categoryService, hashtagService);

		model.addAttribute("posts", returningJSON);

		return "allposts";
	}

	@GetMapping("/list/{id}")
	public String listPostsById(Model model, @PathVariable(value = "id") int id) {

		// TODO Must have an input to choose the prefered order
		String orderType;

		if (model.getAttribute("sort") != null)
			orderType = (String) model.getAttribute("sort");
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

		model.addAttribute("titulo", returningJSON.getTitle());

		model.addAttribute("post", returningJSON);
		model.addAttribute("froalaIsNeed", true);


		Post post = postService.findReturnPostById(id);
		model.addAttribute("likes", reactionService.getLikesPost(post));
		model.addAttribute("dislikes", reactionService.getDislikesPost(post));
		
		Map<String, Integer> likesOfComment = new HashMap<String,Integer>();
		Map<String, Integer> dislikesOfComment = new HashMap<String,Integer>();
		
		for(Comments comment : post.getComments()) {		
			String commentIdString = ""+comment.getId();
			likesOfComment.put(commentIdString, reactionService.getLikesComment(comment));
			dislikesOfComment.put(commentIdString, reactionService.getDislikesComment(comment));
			
		}
		model.addAttribute("likesComments", likesOfComment);
		model.addAttribute("dislikesComments", dislikesOfComment);
		

		utils.addDataToMenu(model, categoryService, hashtagService);
		
		return "postdetails";
	}

	@GetMapping("/list/{id}/sortComments/{sort}")
	public String returnSortedPostList(Model model, @PathVariable(value = "id") int id,
			@PathVariable(value = "sort") String sort) {
		model.addAttribute("sort", sort);
		model.addAttribute("froalaIsNeed", true);
		return listPostsById(model, id);
	}

	@GetMapping("/fromCategory/{id}")
	public String listPostsFromCategory(Model model, @PathVariable(value = "id") int id) {

		List<CategoryDetails> returningJSON = categoryService.findPostsOfCategoryById(id);

		List<showPosts> returningPostJSON = returningJSON.get(0).getPosts();

		model.addAttribute("titulo", returningJSON.get(0).getName());


		utils.addDataToMenu(model, categoryService, hashtagService);

		model.addAttribute("posts", returningPostJSON);

		return "allposts";
	}

	@GetMapping("/fromHashtag/{hashtag}")
	public String listPostsFromHashtag(Model model, @PathVariable(value = "hashtag") String hashtag) {

		PostsOfHashtag hash = hashtagService.findPostOfHashtagByName(hashtag);

		List<PostDetails> returningPostJSON = hash.getPosts();

		model.addAttribute("titulo", "#" + hashtag);

		utils.addDataToMenu(model, categoryService, hashtagService);
		
		model.addAttribute("posts", returningPostJSON);

		return "allposts";
	}
	
	@GetMapping("/newPost")
	public String createNewPost(Model model) {

		
	    model.addAttribute("titulo", "Create post : ");	    
	    model.addAttribute("categories", categoryService.findAll());


	    model.addAttribute("post", new Post());
		utils.addDataToMenu(model, categoryService, hashtagService);
		model.addAttribute("froalaIsNeed", true);
		return "forms/newPost";
	}
	
	@GetMapping("/banComment/{idComment}")
	public String banComment(HttpServletRequest request, Model model, @PathVariable(value = "idComment") int idComment) {
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

		Comments comment = commentService.findCommentById(idComment);
		
		if(loggedInUser.getAuthorities().toString().contains("ROLE_MODERATOR")){
			comment.setRemovedByModerator(true);
			commentService.save(comment);
		}
		
		return "redirect:" + request.getHeader("Referer");
	}
	
	@PostMapping("/newPost")
	public String createNewPostPost(Post post, BindingResult result, Model model) {
		/*
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String loggedUserUsername = loggedInUser.getName();
		User loggedUser = userService.getUserByUsername(loggedUserUsername);
		*/

		User loggedUser = userService.getLoggedUser();
		
		
		int categoryId = Integer.parseInt((String)result.getFieldValue("category"));
		String hashtagString = (String)result.getFieldValue("hashtags");
		
	    List<String> tokens = new ArrayList<>();

		Category categoryToInsert = categoryService.findCategoryById(categoryId);
		System.out.println(categoryToInsert.getName());
		System.out.println(categoryToInsert.getName());
		
		post.setCategory(categoryToInsert);
		post.setCreatedAt(LocalUtils.getActualDate());
		// TODO Should be created by the user that is LoggedIN!!!!
		
		post.setCreatedBy(loggedUser);
		
	    postService.savePost(post);
		
	    StringTokenizer tokenizer = new StringTokenizer(hashtagString, ",");
	    
	    List<Hashtag> hashtagList = null;
	    
	    while (tokenizer.hasMoreElements()) {
	        
	    	Hashtag hash = null;
	    	String hashtagStr = tokenizer.nextToken().trim();
	    	
	        try {
		    	
	        	hash = hashtagService.findHashtagByName(hashtagStr);
	        	//hashtagList.add(hash);
	        	

				hash.getPosts().add(post);
				hashtagService.save(hash);
	        	
	        }catch(NullPointerException e) { 
	        	
	        	hash = new Hashtag(hashtagStr, post);
				hashtagService.save(hash);
				
	        }
	    }
				

		utils.addDataToMenu(model, categoryService, hashtagService);
		model.addAttribute("froalaIsNeed", true);
		return "redirect:/post/list/" + post.getId();
	}
//////////////////////////////////////////////////////////////////////////////////

	@PostMapping("/sendComment")
	public String newComment(HttpServletRequest request, Model model) {
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		String loggedUserUsername = loggedInUser.getName();
		User loggedUser = userService.getUserByUsername(loggedUserUsername);
		

		String getPostId = null;
		StringTokenizer tokenizer = new StringTokenizer(request.getHeader("Referer"), "//");
		while (tokenizer.hasMoreElements()) {
			getPostId = tokenizer.nextToken();
		}
		if (getPostId == null)
			throw new RuntimeException();

		String commentContent = request.getParameter("content");

		Comments newComment = new Comments(commentContent, loggedUser,
				postService.findReturnPostById(Integer.parseInt(getPostId)));

		commentService.save(newComment);

		return "redirect:" + request.getHeader("Referer");
	}


	
////////////////////////////////////////////////////////////////////////////////////////////////////

}
