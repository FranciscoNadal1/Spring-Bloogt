package com.blog.project.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.blog.project.app.entities.Role;

public interface ITrending extends BaseRepository <Role, Long> {

	@Query(value = "SELECT comment_id FROM reaction\r\n" + 
			"where reaction.dtype = 'commentreaction' \r\n" + 
			"and reaction.reaction = true\r\n" + 
			"and reaction.created_at >= DATE_SUB(NOW(),INTERVAL 1 HOUR) \r\n" + 
			"group by reaction.comment_id\r\n" + 
			"order by count(*) desc\r\n" + 
			"limit ?1,?2", nativeQuery = true)		
	List<Integer> getMoreLikedCommentsLastHour(int startAt, int limit);	
	
	@Query(value = "SELECT post_id FROM reaction, post, category where reaction.dtype = 'postreaction'	and reaction.reaction = true	and reaction.created_at >= DATE_SUB(NOW(),INTERVAL 1 HOUR) 	and post.id = reaction.post_id	and post.category_id = category.id	and category.name = ?1	group by reaction.post_id	order by count(*) desc	limit ?2, ?3", nativeQuery = true)	
	List<Integer> getMoreLikedPostsLastHourOfCategory(String categoryName, int startAt, int limit);	
	
	@Query(value = "SELECT post.id 	FROM post,category	where post.category_id = category.id and	category.name not like ?1	order by post.created_at desc	limit ?2, ?3", nativeQuery = true)	
	List<Integer> getLastPostsExceptCategory(String categoryName, int startAt, int limit);	

	
	@Query(value = "SELECT hashtag.name, count(*) as hashtagCount  from post, hashtag, hashtag_post	where post.id=hashtag_post.post_id 	and hashtag.id = hashtag_post.hashtag_id	and post.created_at >= DATE_SUB(NOW(),INTERVAL 1 HOUR)	group by hashtag.name	order by count(*) desc limit ?1, ?2", nativeQuery = true)	
	List<String> getTrendingHashtagLastHour(int startAt, int limit);	
	
	
}
