package com.blog.project.app.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerMapping;

import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.models.service.ICategoryService;
import com.blog.project.app.models.service.ICommentsService;
import com.blog.project.app.models.service.IHashtagService;
import com.blog.project.app.models.service.IPostService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.controllers.UserController;

@Service
public class LocalUtils {

	@Autowired
	private IPostService postService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICommentsService commentService;

	@Autowired
	private IHashtagService hashtagService;

	
	
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public static void ThrowPayloadEmptyException(HttpServletRequest request) {
		logger.warn("Controlled exception - - -   "
				+ (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
				+ "   - - - Had an empty payload");
		throw new NoPayloadDataException();
	}

	public static Date getActualDate() {

		return new Date();
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String formatContentForList(String content) {

		String cleanContent = content.replaceAll("\\<.*?\\>", "");
		try {
			String cleanCutContent = cleanContent.substring(0, 500);
			cleanCutContent = cleanCutContent.concat("...");
			return cleanCutContent;
		} catch (Exception e) {
			return cleanContent;
		}
	}

	public void addDataToMenu(Model model){

		model.addAttribute("categoriesForMenu", categoryService.findAllProjectedBy());
		model.addAttribute("hashtagsForMenu", hashtagService.findAllProjectedBy());
		
		
	}
	
	public void addDataToMenu(Model model, /*IPostService postService,  IUserService userService, ICommentsService commentService,*/ ICategoryService categoryService, IHashtagService hashtagService){

		model.addAttribute("categoriesForMenu", categoryService.findAllProjectedBy());
		model.addAttribute("hashtagsForMenu", hashtagService.findAllProjectedBy());
		
		
	}

}
