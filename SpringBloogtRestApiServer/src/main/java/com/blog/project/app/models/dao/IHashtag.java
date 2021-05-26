package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Hashtag.HashtagNoDetails;
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

	@Query(value = "select * from hashtag where hashtag.name like  %?1% limit 0, ?2", nativeQuery = true)					
	List<HashtagNoDetails> searchHashtagByString(String hashtagPart, int results);
}
