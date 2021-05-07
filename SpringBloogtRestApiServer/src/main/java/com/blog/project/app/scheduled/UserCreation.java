package com.blog.project.app.scheduled;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;
import com.github.javafaker.Faker;

@Service
public class UserCreation {


	@Autowired
	private IUserService userService;

	@Autowired
	private IPostService postService;

	@Autowired
	private ICategoryService categoryService;

	
	public void createBot() {
		User newUser = new User();

		String username =  "PosterBot";
			newUser.setUsername(username);						


		newUser.setName("Hello");
		newUser.setAvatar("https://pngimg.com/uploads/robot/robot_PNG92.png");
		newUser.setSurname("I post stuff");
		

		newUser.setPassword("123456");
		
		newUser.setCreatedAt((Date) LocalUtils.getActualDate());	

		newUser.setEmail("bot@posterbot.com");
		
		userService.saveUserAndAssignRole(newUser,"ROLE_USER");
		
	//	userService.followUser("PosterBot");
		/*
		User adminUser = userService.getUserByUsername("admin");
		if(adminUser.getFollowing()==null)
		adminUser.getFollowing().add(newUser);
		userService.save(adminUser);
		*/
		
	}

	@Scheduled(fixedRate = 100000)
	public void createRandomPostsForBot() {
		String postTitle = "randomtitle";
		Post newPost = new Post();
		
		newPost.setCategory(categoryService.findCategoryByName("QuickPost"));
		newPost.setTitle(postTitle);

	//	 byte[] array = new byte[7]; // length is bounded by 7
	//	 new Random().nextBytes(array);
		// String generatedString = new String(lorem.getWords(5, 10));
				
		newPost.setContent("sss");
		newPost.setTimesViewed(0);
		newPost.setCreatedAt((Date) LocalUtils.getActualDate());
		newPost.setCreatedBy(userService.findReturnUserById(5));
		
		

		postService.savePost(newPost);
	}
}
