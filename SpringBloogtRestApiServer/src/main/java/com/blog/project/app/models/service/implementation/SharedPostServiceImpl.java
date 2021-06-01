package com.blog.project.app.models.service.implementation;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.SharedPost;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.dao.ISharedPost;
import com.blog.project.app.models.service.INotificationService;
import com.blog.project.app.models.service.ISharedPostService;

@Service
public class SharedPostServiceImpl implements ISharedPostService{

	@Autowired
	ISharedPost sharedPostDao;

	@Autowired
	private INotificationService notificationService;
	
	@Transactional
	@Override
	public void sharePost(User user, Post post) {
		SharedPost sharedPost = new SharedPost();
		sharedPost.setCreatedAt(new Date());
		sharedPost.setPost(post);
		sharedPost.setSharedBy(user);
		
		
		sharedPostDao.save(sharedPost);
		notificationService.newNotificationUserObject("share", post.getCreatedBy(), user, post);
	}
	
	
	@Transactional
	@Override
	public void unSharePost(User user, Post post) {
		SharedPost sharedPost = sharedPostDao.findBySharedByAndPost(user, post);
		
		
		sharedPostDao.delete(sharedPost);
		notificationService.deleteNotification(user, post, "share");
	}
}
