package com.blog.project.app.models.service;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;

public interface ISharedPostService {
	public void sharePost(User user, Post post) ;
	public void unSharePost(User user, Post post) ;
}
