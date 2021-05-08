package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Post.showPosts;

public interface ITrendingService {
	List<showPosts> getMoreVotedPostsLastHour(String category, int start, int end);
	List<showPosts> getLastPostsExceptCategory(String category, int start, int end);
	List<String> getTrendingLastHour(int start, int end);
}
