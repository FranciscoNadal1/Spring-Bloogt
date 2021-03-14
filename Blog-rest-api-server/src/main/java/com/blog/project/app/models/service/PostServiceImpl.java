package com.blog.project.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.PostDetails;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.User;
import com.blog.project.app.entities.User.UserComments;
import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.entities.User.UserPosts;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.IUser;

@Service
public class PostServiceImpl implements IPostService {

	@Autowired
	private IPost postDao;
	
	@Override
	public List<Post> findAll() {
		return (List<Post>) postDao.findAll();
	}

	@Override
	public void save(Post post) {
		postDao.save(post);
	}

	@Override
	public List<PostDetails>  findOne(int id) {
		return (List<PostDetails>)postDao.findById(id);
	}

	@Override
	public void delete(Long id) {
		postDao.deleteById(id);

	}


	
//////////////////////////////////////////////	
// CUSTOM
	@Override
	public Post  findReturnPostById(int id) {
		return (Post)postDao.findReturnPostById(id);
	}

	@Override
	public List<showPosts> findAllProjectedBy() {
		return (List<showPosts>) postDao.findAllProjectedBy();
	}
}
