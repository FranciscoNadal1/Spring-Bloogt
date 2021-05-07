package com.blog.project.app.models.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.service.IPostService;

@Service
public class PostServiceImpl implements IPostService {

	@Autowired
	private IPost postDao;

	@Override
	public List<Post> findAllPosts() {
		return (List<Post>) postDao.findAll();
	}

	@Override
	public void savePost(Post post) {
		postDao.save(post);
	}

	@Override
	public showPosts findPostById(int id) {
		return (showPosts) postDao.findById(id);
	}

	@Override
	public void deletePost(Long id) {
		postDao.deleteById(id);

	}

//////////////////////////////////////////////	

	@Override
	public showPosts findPostByIdAndSortByCreatedDateAsc(int id) {
		return (showPosts) postDao.findByIdOrderByCreatedAtAsc(id);
	}

	@Override
	public showPosts findPostByIdAndSortByCreatedDateDesc(int id) {
		return (showPosts) postDao.findByIdOrderByCreatedAtDesc(id);
	}

// CUSTOM
	@Override
	public Post findReturnPostById(int id) {
		return (Post) postDao.findReturnPostById(id);
	}

	@Override
	public List<showPosts> findAllPostsProjection() {
		return (List<showPosts>) postDao.findAllProjectedByOrderByCreatedAtDesc();
	}
	
	@Override
	public List<showPosts> findAllPostsProjectionByCategory(Category category) {
		return (List<showPosts>) postDao.findAllProjectedByCategory(category);
	}
	@Override
	public List<showPosts> findAllPostsProjectionByCategoryNot(Category category) {
		return (List<showPosts>) postDao.findAllProjectedByCategoryNot(category);
	}
	
	@Override
	public void deletePostById(int id) {
			postDao.deleteById(id);
	}

	@Override
	public List<showPosts> findAllPostsOfFollowingUser(List<User> users){
		return (List<showPosts>) postDao.findByCreatedByIn(users);
	}

	@Override
	public List<showPosts> findAllPostsOfFollowingUserAndCategory(List<User> users, Category category){
		return (List<showPosts>) postDao.findByCreatedByInAndCategoryOrderByCreatedAtDesc(users, category);
	}
	
	@Override
	public void addVisit(int id) {
		Post post = postDao.findReturnPostById(id);
		
		post.addVisit();
		postDao.save(post);
		
		
	}
}
