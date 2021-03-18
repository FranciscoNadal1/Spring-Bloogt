package com.blog.project.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.service.ICategoryService;
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

	@GetMapping("/userlist")
	public String listUsers(Model model) {
		model.addAttribute("titulo", "List of users");

		List<UserData> returningJSON = userService.findAllProjectedBy();

		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();
		addDataToMenu(model);

		model.addAttribute("users", returningJSON);

		return "listofusers";
	}

	@GetMapping("/profile/{username}")
	public String userProfile(Model model, @PathVariable(value = "username") String username) {
		model.addAttribute("titulo", "Profile");

		UserData returningJSON = userService.findByUsername(username);
		int idUser = Integer.parseInt(returningJSON.getId());

		List<UserComments> allCommentsOfUser = userService.findAllCommentsOfUserProjectedById(idUser);
		List<UserPosts> allPostsOfUser = userService.findAllPostsOfUserProjectedById(idUser);

		model.addAttribute("user", returningJSON);
		model.addAttribute("comments", allCommentsOfUser);
		model.addAttribute("posts", allPostsOfUser);

		List<CategoryList> categoriesForMenu = categoryService.findAllProjectedBy();
		addDataToMenu(model);

		System.out.println(returningJSON.getEmail());

		return "profile";
	}

	@GetMapping("/newUser")
	public String createUser(Model model) {

	    model.addAttribute("user", new User());
		model.addAttribute("titulo", "New User : ");
		addDataToMenu(model);

		return "forms/createUser";
	}
	
	
	@PostMapping("/newUser")
	public String submitCreateUser(@Valid User user, BindingResult result, Model model) {

		// We add the fields the user couldn't
		user.setRole("ROLE_USER");
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
		addDataToMenu(model);

		userService.save(user);

		return "redirect:userlist";
	}
////////////////////////////////////////////////////////////////////////////////////////////////////
	public Model addDataToMenu(Model model) {

		model.addAttribute("categoriesForMenu", categoryService.findAllProjectedBy());
		model.addAttribute("hashtagsForMenu", hashtagService.findAllProjectedBy());

		return model;

	}

}
