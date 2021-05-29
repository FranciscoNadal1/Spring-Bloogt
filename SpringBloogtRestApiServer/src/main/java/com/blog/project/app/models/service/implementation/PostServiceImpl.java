package com.blog.project.app.models.service.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

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
		for (SharedPostProjection sharedPost : sharedPosts) {
			showPosts sharedProjectionPost = sharedPost.getPost();
			sharedProjectionPost.setIsShared(true);
			sharedProjectionPost.setSharedAt(sharedPost.getCreatedAt());
			//sharedProjectionPost.se(sharedPost.getCreatedAt());			
			sharedProjectionPost.setSharedBy(sharedPost.getSharedBy());

			listOfShowPosts.add(sharedProjectionPost);
			

		}
	//	listOfShowPosts.sort(c);
		
		
		List<showPosts> listWithoutDuplicates = new ArrayList<>(
			      new HashSet<>(listOfShowPosts));
		
//		listWithoutDuplicates.sort(Comparator.comparing(showPosts::getCreatedAt).reversed());
//		Collections.reverse(listWithoutDuplicates);

	    Collections.sort(listWithoutDuplicates, new Post.PostSortingComparator().reversed());
		//listWithoutDuplicates.sor
//		Collections.sort(listWithoutDuplicates, new showPosts);
		return listWithoutDuplicates;
	}

	@Override
	public void addVisit(int id) {
		Post post = postDao.findReturnPostById(id);

		post.addVisit();
		postDao.save(post);

	}
}
