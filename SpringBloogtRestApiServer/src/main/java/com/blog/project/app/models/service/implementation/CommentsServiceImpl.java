package com.blog.project.app.models.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Comments.ShowAllComments;
import com.blog.project.app.entities.Comments.ShowComments;
import com.blog.project.app.entities.Post;
import com.blog.project.app.models.dao.IComments;
import com.blog.project.app.models.service.ICommentsService;

@Service
public class CommentsServiceImpl implements ICommentsService {

	@Autowired
	private IComments commentsDao;

	@Override
	public List<Comments> findAll() {
		return (List<Comments>) commentsDao.findAll();
	}

	@Override
	public void save(Comments Comments) {
		commentsDao.save(Comments);
	}

	@Override
	public List<ShowAllComments> findOne(int id) {
		return (List<ShowAllComments>) commentsDao.findById(id);
	}

	@Override
	public void delete(Long id) {
		commentsDao.deleteById(id);

	}

	@Override
	public List<ShowAllComments> findAllProjectedBy() {
		return (List<ShowAllComments>) commentsDao.findAllProjectedBy();
	}

	@Override
	public Comments findCommentById(int id) {
		return commentsDao.findCommentById(id);
	}

	@Override
	public List<ShowComments> findCommentByPost(Post post) {
		return commentsDao.findByPost(post);
	}

//////////////////////////////////////////////	
// CUSTOM

}
