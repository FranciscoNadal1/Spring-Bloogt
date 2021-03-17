package com.blog.project.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;
import com.blog.project.app.models.dao.IHashtag;

@Service
public class HashtagServiceImpl implements IHashtagService {

	@Autowired
	private IHashtag hashtagDao;

	@Override
	public List<Hashtag> findAll() {
		return (List<Hashtag>) hashtagDao.findAll();
	}

	@Override
	public void save(Hashtag hashtag) {
		hashtagDao.save(hashtag);
	}

	@Override
	public List<HashtagShow> findOne(int id) {
		return (List<HashtagShow>) hashtagDao.findById(id);
	}

	@Override
	public void delete(Long id) {
		hashtagDao.deleteById(id);

	}

//////////////////////////////////////////////	
// CUSTOM

	@Override
	public List<HashtagShow> findAllProjectedBy() {
		return (List<HashtagShow>) hashtagDao.findAllProjectedBy();
	}

	@Override
	public List<PostsOfHashtag> findAllPostsOfHashtagById(int id) {
		return (List<PostsOfHashtag>) hashtagDao.findAllPostsOfHashtagById(id);
	}

	@Override
	public List<PostsOfHashtag> findAllPostsOfHashtagByName(String name) {
		return (List<PostsOfHashtag>) hashtagDao.findAllPostsOfHashtagByName(name);
	}

	@Override
	public Hashtag findHashtagById(int id) {
		return (Hashtag) hashtagDao.findHashtagById(id);
	}

	@Override
	public Hashtag findHashtagByName(String str) {
		return (Hashtag) hashtagDao.findHashtagByName(str);
	}
}
