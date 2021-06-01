package com.blog.project.app.rest.controllers;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.models.dao.ITrending;
import com.blog.project.app.models.service.ITrendingService;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

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
/*
		if (listPosts.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);
*/
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
	
	@GetMapping("/trendingLastHour/{start}/{end}")
	public JSONObject getTrendingLastHour(HttpServletResponse response, HttpServletRequest request, 
			@PathVariable(value = "start") int start, 
			@PathVariable(value = "end") int end) {

		List<String> listTrendings = trendingService.getTrendingLastHour(start, end);
		/*
		if (listTrendings.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);
*/
		JSONObject responseJson = new JSONObject();
		
		List<JSONObject> listJSONObjects = new LinkedList<>();
		
		for(String str : listTrendings) {

			JSONObject responseJson2 = new JSONObject();
			String strArr[] = str.split(",");			

			responseJson2.appendField("name", strArr[0]);
			responseJson2.appendField("count", strArr[1]);
			listJSONObjects.add(responseJson2);
	
		}
		
		responseJson.appendField("ListTrendings", listJSONObjects);
		
		responseJson.appendField("status", "OK");
		return responseJson;		
	}
	
}
