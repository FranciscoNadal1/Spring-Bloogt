package com.blog.project.app.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerMapping;

import com.blog.project.app.errors.NoPayloadDataException;
import com.blog.project.app.rest.controllers.UserController;

public class LocalUtils {

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
}