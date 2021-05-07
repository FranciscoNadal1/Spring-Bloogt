package com.blog.project.app.models.service.implementation;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.ITrending;
import com.blog.project.app.models.service.ITrendingService;

@Service
public class TrendingServiceImpl implements ITrendingService{
	
	@Autowired
	ITrending trendingDao;
	
	@Autowired
	IPost postDao;
	
	public List<showPosts> getMoreVotedPostsLastHour(String category, int start, int end){
		List<Integer> listInt = trendingDao.getMoreLikedPostsLastHourOfCategory(category, start, end);
		List<showPosts> listPosts = new LinkedList<>();
		
		
		for(Integer indInt : listInt) {
			showPosts post = postDao.findById(indInt);
			listPosts.add(post);
		}
		
		return listPosts;
		
	}

	@Override
	public List<showPosts> getLastPostsExceptCategory(String category, int start, int end) {
		List<Integer> listInt = trendingDao.getLastPostsExceptCategory(category, start, end);
		List<showPosts> listPosts = new LinkedList<>();
		
		
		for(Integer indInt : listInt) {
			showPosts post = postDao.findById(indInt);
			listPosts.add(post);
		}
		
		return listPosts;
	}

}
