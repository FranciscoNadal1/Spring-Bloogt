package com.blog.project.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Role;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.entities.chat.Chat;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.IChatMessageService;
import com.blog.project.app.models.service.IHashtagService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

//@RequestMapping("/website")
@Controller
public class UserManagement {

	@Autowired
	private IUserService userService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IHashtagService hashtagService;
	
	@Autowired
	private IChatMessageService chatMessageService;

	@Autowired
	private LocalUtils utils;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@GetMapping("/userlist")
	public String listUsers(Model model) {
		model.addAttribute("titulo", "List of users");

		List<UserData> returningJSON = userService.findAllProjectedBy();

		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();
		utils.addDataToMenu(model, categoryService, hashtagService);

		model.addAttribute("users", returningJSON);

		return "listofusers";
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////			PROFILE
	@GetMapping("/profile/{username}/following")
	public String followingProfile(Model model, @PathVariable(value = "username") String username) {

		User profileUser = userService.getUserByUsername(username);
		

			model.addAttribute("profileUser", profileUser);	
			model.addAttribute("titulo", username + " is following");	

		return "/profile/following";		
	}
	
	@GetMapping("/profile/{username}/followers")
	public String followersProfile (Model model, @PathVariable(value = "username") String username) {

		model.addAttribute("titulo", username + " is followed by :");
		List<User> followedBy = userService.getUsersThatFollowUser(username);
		model.addAttribute("followedBy", followedBy);
		return "/profile/followers";	
	}	
	
	
	@GetMapping("/profile/{username}")
	public String userProfile(Model model, @PathVariable(value = "username") String username) {
		
		
		User loggedUser = null;
		try {
		 loggedUser = userService.getLoggedUser();
		}catch(Exception e) {

		}
		List<User> followedBy = userService.getUsersThatFollowUser(username);
		
		if(loggedUser != null)
			if(followedBy.contains(loggedUser))
				model.addAttribute("followingThisUser", true);
			else
				model.addAttribute("followingThisUser", false);
				/*		
		if(loggedUser != null) {
			if(loggedUser.getUsername().equals(username))
				model.addAttribute("titulo", "Your profile");			
			else
				model.addAttribute("titulo", "Profile");	
		}else*/
			model.addAttribute("titulo", "Profile");	


		

		UserData returningJSON = userService.getUserDataByUsername(username);
		int idUser = Integer.parseInt(returningJSON.getId());

		List<UserComments> allCommentsOfUser = userService.findAllCommentsOfUserProjectedById(idUser);
		UserPosts allPostsOfUser = userService.findAllPostsOfUserProjectedById(idUser);

		model.addAttribute("user", returningJSON);
		model.addAttribute("comments", allCommentsOfUser);
		model.addAttribute("posts", allPostsOfUser);

		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();

		model.addAttribute("followedBy", followedBy);
		
		utils.addDataToMenu(model, categoryService, hashtagService);

		System.out.println(returningJSON.getEmail());

		return "profile";
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@GetMapping("/newUser")
	public String createUser(Model model) {

	    model.addAttribute("user", new User());
		model.addAttribute("titulo", "New User : ");
		utils.addDataToMenu(model, categoryService, hashtagService);

		return "forms/createUser";
	}

	
	
	
	@GetMapping("/follow/{username}")
	public String followUser(Model model, @PathVariable(value = "username") String username) {
		
		
		userService.followUser(username);

	   // model.addAttribute("user", new User());
		model.addAttribute("titulo", "New User : ");
		utils.addDataToMenu(model, categoryService, hashtagService);

		return "redirect:/profile/" + username;
	}

	
	
	
	@PostMapping("/newUser")
	public String submitCreateUser(@Valid User user, BindingResult result, Model model) {

		user.setCreatedAt(LocalUtils.getActualDate());
		///////////////////////////////////////////////
		
		
		if(result.hasErrors()){
			for(ObjectError err : result.getAllErrors()) {
				System.out.println(err);
			}

			return "forms/createUser";
		}

	    model.addAttribute("user", user);
		
	    model.addAttribute("titulo", "New User : ");
		utils.addDataToMenu(model, categoryService, hashtagService);

		if(userService.getUserByUsername(user.getUsername()) != null) 
			throw new RuntimeException("Username already taken");

		if(!userService.findByEmail(user.getEmail()).isEmpty()) 
			throw new RuntimeException("Email is already in use");
		
		
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userService.saveUserAndAssignRole(user,"ROLE_USER");

		return "redirect:userlist";
	}


}
