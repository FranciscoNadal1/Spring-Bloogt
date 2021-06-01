package com.blog.project.app.models.service.implementation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Category;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.Post.showPosts;
import com.blog.project.app.entities.SharedPost.SharedPostProjection;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.ISharedPost;
import com.blog.project.app.models.service.IPostService;

@Service
public class PostServiceImpl implements IPostService {

	@Autowired
	private IPost postDao;

	@Autowired
	ISharedPost sharedPostDao;

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
	public List<showPosts> findAllPostsOfFollowingUser(List<User> users) {
		List<showPosts> listOfShowPosts = postDao.findByCreatedByIn(users);
		System.out.println(users);
		List<SharedPostProjection> sharedPosts = sharedPostDao.findPostOfSharedPostBySharedByIn(users);

		for (SharedPostProjection sharedPost : sharedPosts) {
			listOfShowPosts.add(sharedPost.getPost());

			System.out.println(sharedPost);
		}

		return listOfShowPosts;
	}

	@Override
	public List<showPosts> findAllPostsOfFollowingUserAndCategory(List<User> users, Category category) {

		List<showPosts> listOfShowPosts = postDao.findByCreatedByInAndCategoryOrderByCreatedAtDesc(users, category);

		List<SharedPostProjection> sharedPosts = sharedPostDao.findPostOfSharedPostBySharedByIn(users);

		List<showPosts> objectsToRemove = new LinkedList<>();
//		Integer i = 0;
		for (SharedPostProjection sharedPost : sharedPosts) {
			showPosts sharedProjectionPost = sharedPost.getPost();
			sharedProjectionPost.setIsShared(true);
			sharedProjectionPost.setSharedAt(sharedPost.getCreatedAt());
			//sharedProjectionPost.se(sharedPost.getCreatedAt());			
			sharedProjectionPost.setSharedBy(sharedPost.getSharedBy());

			//sharedPosts.remove(sharedPost);
			listOfShowPosts.add(sharedProjectionPost);		
			
			//listOfShowPosts.remove(sharedPost.getPost());
		//	objectsToRemove.add(sharedPost.getPost());
	//		i = i+1;
		}
	//	sharedPosts.removeAll(objectsToRemove);
		
		
		/*
		for(int a=0;a!=list.size();a++) {
			System.out.println("Post repetido:" + listOfShowPosts.get(a).getId());
			listOfShowPosts.remove(list.get(a));
		}*/
		/*
		for(Integer integer : list) {
			listOfShowPosts.rem
			listOfShowPosts.remove(integer);
		}
		*/
/*
 		for (SharedPostProjection sharedPost : sharedPosts) {
			showPosts sharedProjectionPost = sharedPost.getPost();
			sharedProjectionPost.setIsShared(true);
			sharedProjectionPost.setSharedAt(sharedPost.getCreatedAt());
			//sharedProjectionPost.se(sharedPost.getCreatedAt());			
			sharedProjectionPost.setSharedBy(sharedPost.getSharedBy());

			//sharedPosts.remove(sharedPost);
			listOfShowPosts.add(sharedProjectionPost);		

		}		
 */
		
	
		List<showPosts> listWithoutDuplicates = listOfShowPosts.stream().distinct().collect(Collectors.toList());
	

	    Collections.sort(listWithoutDuplicates, new Post.PostSortingComparator().reversed());
	
		return listWithoutDuplicates;
	}

	@Override
	public void addVisit(int id) {
		Post post = postDao.findReturnPostById(id);

		post.addVisit();
		postDao.save(post);

	}
}
