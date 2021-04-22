package com.blog.project.app.models.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
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
	public PostDetails findPostById(int id) {
		return (PostDetails) postDao.findById(id);
	}

	@Override
	public void deletePost(Long id) {
		postDao.deleteById(id);

	}

//////////////////////////////////////////////	

	@Override
	public PostDetails findPostByIdAndSortByCreatedDateAsc(int id) {
		return (PostDetails) postDao.findByIdOrderByCreatedAtAsc(id);
	}

	@Override
	public PostDetails findPostByIdAndSortByCreatedDateDesc(int id) {
		return (PostDetails) postDao.findByIdOrderByCreatedAtDesc(id);
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
	public void deletePostById(int id) {
			postDao.deleteById(id);
	}

	@Override
	public void addVisit(int id) {
		Post post = postDao.findReturnPostById(id);
		
		post.addVisit();
		postDao.save(post);
		
		
	}
}