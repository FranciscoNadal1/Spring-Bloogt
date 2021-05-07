package com.blog.project.app.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.reaction.PostReaction.ReactionPostByUser;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.dao.ITrending;
import com.blog.project.app.models.service.ITrendingService;
import com.blog.project.app.utils.LocalUtils;

@RestController
@CrossOrigin
@RequestMapping("/api/trending")
public class TrendingController {


	@Autowired
	ITrending trendingDao;
	

	@Autowired
	ITrendingService trendingService;
	
	@GetMapping("/betterPost/{category}/{start}/{end}")
	public List<showPosts> getBetterPosts(HttpServletResponse response, HttpServletRequest request, 
			@PathVariable(value = "category") String category, 
			@PathVariable(value = "start") int start, 
			@PathVariable(value = "end") int end) {

		//List<Integer> listPosts = trendingDao.getMoreLikedPostsLastHourOfCategory(category, start, end);
		List<showPosts> listPosts = trendingService.getMoreVotedPostsLastHour(category, start, end);
		
		if (listPosts.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return listPosts;
		
	}
	@GetMapping("/lastsPostsExceptCategory/{category}/{start}/{end}")
	public List<showPosts> getAllPostsExceptCategory(HttpServletResponse response, HttpServletRequest request, 
			@PathVariable(value = "category") String category, 
			@PathVariable(value = "start") int start, 
			@PathVariable(value = "end") int end) {

		//List<Integer> listPosts = trendingDao.getMoreLikedPostsLastHourOfCategory(category, start, end);
		List<showPosts> listPosts = trendingService.getLastPostsExceptCategory(category, start, end);
		
		if (listPosts.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return listPosts;
		
	}
}
