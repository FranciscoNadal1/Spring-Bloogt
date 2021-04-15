package com.blog.project.app.models.dao;

import java.util.List;

import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Hashtag.HashtagShow;
import com.blog.project.app.entities.Hashtag.PostsOfHashtag;

public interface IHashtag extends BaseRepository <Hashtag, Long> {


	List<HashtagShow> findById(int id);
	Iterable<Hashtag> findAll();
	List<HashtagShow> findAllProjectedBy();

	List<PostsOfHashtag> findAllPostsOfHashtagById(int id);
	PostsOfHashtag findPostOfHashtagByName(String name);
	
	Hashtag findHashtagById(int id);

	Hashtag findHashtagByName(String str);

}
