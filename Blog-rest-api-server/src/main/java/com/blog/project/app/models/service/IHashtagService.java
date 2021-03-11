package com.blog.project.app.models.service;

import java.util.List;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Category.CategoryDetails;
import com.blog.project.app.entities.Category.CategoryList;
import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;

public interface IHashtagService {
/// Default
	public List<Hashtag> findAll();

	public void save(Hashtag hashtag);

	public void delete(Long id);


/// Custom
	public List<HashtagShow> findOne(int id);
	public List<HashtagShow> findAllProjectedBy();


	public List<PostsOfHashtag> findAllPostsOfHashtagById(int id);
	public List<PostsOfHashtag> findAllPostsOfHashtagByName(String name);
}
