package com.blog.project.app.scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.Comments;
import com.blog.project.app.entities.Hashtag;
import com.blog.project.app.entities.Post;
import com.blog.project.app.entities.User;
import com.blog.project.app.models.dao.IComments;
import com.blog.project.app.models.dao.IPost;
import com.blog.project.app.models.dao.IUser;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IHashtagService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IReactionService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;
import com.blog.project.app.utils.RandomData;

@Service
@Transactional
@EnableAsync
@ConditionalOnProperty(value = "simulate.activity", havingValue = "true")
public class SimulateActivity {


	private static final Logger logger = LoggerFactory.getLogger(SimulateActivity.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IHashtagService hashtagService;
	
	@Autowired
	private IUser userDao;

	@Autowired
	private IPostService postService;
	@Autowired
	private ICommentsService commentService;

	@Autowired
	private IPost postDao;
	@Autowired
	private IComments commentsDao;

	@Autowired
	private ICategoryService categoryService;
	
	@Autowired
	private IReactionService reactionService;
	
	@Autowired
	RandomData randomData;
	

	@Scheduled(fixedRate = 1000)
	@Async
	public void simulation() {
		Random rand = new Random();
		int int_random = rand.nextInt(10); 

		if((int_random >= 0 && (int_random <= 1)) ) {
			logger.info("Randomly creating Bot");
			this.createBot();
		}
		else if(int_random >= 2 && (int_random <= 3)) {
			logger.info("Randomly creating post of Bot");
			int randomUserId = getRandomUserIdOfBot();
			
			if(randomUserId == -1)
				return;
			
			this.createRandomPostsForBot(getRandomUserIdOfBot());
		}
		else if(int_random >= 4 && (int_random <= 7)) {
			logger.info("Randomly creating comment");			
			this.randomCreateComment();
		}
		else if(int_random >= 8 && (int_random <= 10)) {
			int times = 5;
			for(int i=0;i!=times;i++) {
				try {
					randomReactToPostOfBot();
					logger.info("Randomly liking post");
				} catch (Exception e) {

				}
			}
			for(int i=0;i!=times;i++) {
				try {
					this.randomReactToCommentOfBot();
					logger.info("Randomly liking comment");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}				
		
		else {
			return;
		}
	}
	@Async
	@Transactional
	public void randomCreateComment() {
		int randomUserId = getRandomUserIdOfBot();
		int randomUserIdHasPost = getRandomUserIdOfBotThasHasPosted();
		int randomPost = getRandomPostIdOfBot(randomUserIdHasPost);
		
		if(randomUserIdHasPost == -1 || !(randomPost > 0))
			return;
		
		
		Comments newComment = new Comments();

		newComment.setCreatedAt( LocalUtils.getActualDate());		
		newComment.setMessage(randomData.randomMessage());
		//////////////////////////////////////////////
		
		newComment.setCreatedBy(userService.findReturnUserById(randomUserId));		
		newComment.setPost(postService.findReturnPostById(randomPost));

		commentService.save(newComment);
		
	}
	@Async
	@Transactional
	public void randomReactToPostOfBot() {
		int randomUserId = getRandomUserIdOfBot();
		int randomUserIdHasPost = getRandomUserIdOfBotThasHasPosted();
		if(randomUserId == -1)
			return;
		
		User user = userService.findReturnUserById(randomUserId);
		int randomPostId = getRandomPostIdOfBot(randomUserIdHasPost);
		
		if(randomUserId == -1 || randomPostId == -1)
			return;


		if(new Random().nextInt(10) >= 3 )
			reactionService.likeOrDislikePostOrComment(user, randomPostId, true, "Post");
		else
			reactionService.likeOrDislikePostOrComment(user, randomPostId, false, "Post");
		
	}
	
	@Async
	@Transactional
	public void randomReactToCommentOfBot() {
		int randomUserId = getRandomUserIdOfBot();
		int randomUserIdHasPost = getRandomUserIdOfBotThasHasPosted();
		if(randomUserId == -1)
			return;
		
		User user = userService.findReturnUserById(randomUserId);
		int randomCommentId = getRandomCommentIdOfBot(randomUserIdHasPost);
		
		if(randomUserId == -1 || randomCommentId == -1)
			return;
				
		
		if(new Random().nextInt(10) >= 3 )
			reactionService.likeOrDislikePostOrComment(user, randomCommentId, true, "Comment");
		else
			reactionService.likeOrDislikePostOrComment(user, randomCommentId, false, "Comment");
		
	}	
	
	@Async
	public int getRandomCommentIdOfBot(int userId) {
		User user = userService.findReturnUserById(userId);

		Random rand = new Random();
		int upperLimit = 0;

		upperLimit = commentsDao.findByCreatedBy(user).size()-1;	
		
		if(upperLimit <=0)
			return -1;

		return commentsDao.findByCreatedBy(user).get(rand.nextInt(upperLimit)).getId();
	}		
	
	@Async
	public int getRandomPostIdOfBot(int userId) {
		User user = userService.findReturnUserById(userId);

		Random rand = new Random();
		int upperLimit = 0;

		upperLimit = postDao.findByCreatedBy(user).size()-1;	
		if(upperLimit <=0)
			return -1;

		return postDao.findByCreatedBy(user).get(rand.nextInt(upperLimit)).getId();
	}	
	
	@Async	
	public int getRandomUserIdOfBotThasHasPosted() {
		List<User> bots = userDao.getAllBotsThatHavePosts();
		int upperLimit = bots.size();
		Random rand = new Random();
		

		if(upperLimit==0)
			return -1;
		
		int int_random = rand.nextInt(upperLimit); 
		
		int userID = bots.get(int_random).getId();
		return userID;
	}
	@Async
	public int getRandomUserIdOfBot() {
		List<User> listUsers= userService.findAll();
		List<User> bots = new LinkedList<User>();
		int upperLimit = 0;
		Random rand = new Random();
		
		
		for(User user : listUsers) {
			if(user.getUsername().startsWith("BOT-")) {
				bots.add(user);
				upperLimit++;
			}
		}

		if(upperLimit==0)
			return -1;
		int int_random = rand.nextInt(upperLimit); 
		
		
		return bots.get(int_random).getId();
	}
	
	@Async
	@Transactional
	public void createBot() {
		User newUser = new User();
		Random rand = new Random();
		int int_random = rand.nextInt(5000); 

		List<User> listUsers= userService.findAll();
		String randomUsername = randomData.randomBotUsernameNotAlreadyRegistered(listUsers);
					
			newUser.setUsername(randomUsername);						
					
			String randomFirstName = randomData.randomFirstName();
			String randomLastName = randomData.randomLastName();			
			//String avatar = randomData.getRandomImage();		
			//newUser.setAvatar(avatar);
			

		newUser.setName(randomFirstName);
		newUser.setAvatar("https://pngimg.com/uploads/robot/robot_PNG92.png");
		newUser.setSurname(randomLastName);
		

		newUser.setPassword("123456");
		
		newUser.setCreatedAt((Date) LocalUtils.getActualDate());	

		newUser.setEmail(randomFirstName + "-" + randomLastName + "@posterbot.com");
		
		userService.saveUserAndAssignRole(newUser,"ROLE_USER");
	}


	@Transactional 
	public void createRandomPostsForBot(int userId) {
		Random rand = new Random();
		User user = userService.findReturnUserById(userId);

				String postTitle = "randomtitle";
				Post newPost = new Post();
				
				newPost.setCategory(categoryService.findCategoryByName("QuickPost"));
				newPost.setTitle(postTitle);
						
				String content = randomData.randomMessage();
				newPost.setContent(content);
				
				String[] arr = content.split(" "); 
				List<String> payloadHashtags = new LinkedList<>();
				
				List<String> arrayImages = new ArrayList<>();
				

				int int_random = rand.nextInt(2); 
				
				if(int_random == 1)
					for(int i=0;i!=rand.nextInt(4);i++) {
						arrayImages.add(randomData.getRandomImage());
					}
				
				newPost.setImagePost(arrayImages);
				
				
				newPost.setTimesViewed(0);
				newPost.setCreatedAt((Date) LocalUtils.getActualDate());
				newPost.setCreatedBy(user);				
		
				postService.savePost(newPost);
				
				
				
				for(String word : arr) {
					if(word.startsWith("#") && word.length() >= 3) {
						word = word.substring(1);
						payloadHashtags.add(word);
					}
				}
				
				if(!payloadHashtags.isEmpty())
					for (String hashtag : payloadHashtags) {
						Hashtag hash = null;
			
						try {
							hash = hashtagService.findHashtagByName(hashtag);
			
							hash.getPosts().add(newPost);
							hashtagService.save(hash);
			
						} catch (NullPointerException e) {
			
							hash = new Hashtag(hashtag, newPost);
							hashtagService.save(hash);
						}catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException("Error with the hashtag provided");
						}
					}
				
				
				
				
				
	}
}
