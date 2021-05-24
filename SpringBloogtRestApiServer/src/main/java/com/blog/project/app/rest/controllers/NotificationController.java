package com.blog.project.app.rest.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.Notifications.NotificationDetails;
import com.blog.project.app.entities.User;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.dao.INotifications;
import com.blog.project.app.models.service.INotificationService;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private INotificationService notificationService;

	@Autowired
	private INotifications notificationDao;

	@Autowired
	private JWTHandler jwtHandler;

	@Autowired
	IUserService userService;
	
	@GetMapping("/newTest")
	public String newTestNotifications(HttpServletResponse response, HttpServletRequest request) {

		try {
			

			notificationService.newNotification("follow", "admin", "admin");
		} catch (Exception e) {
			return "not created, " + e.getMessage();
		}

		return "created";
	}
	
	@GetMapping("/count")
	public JSONObject getUnreadNotificationNumberOfLoggedUser(
			@RequestHeader(value="Authorization", required=false) String authorization,
			HttpServletResponse response, 
			HttpServletRequest request) {


		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();
		
		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		try {
			

		} catch (Exception e) {
			throw new RuntimeException("Couldn't get count of notifications");
		}

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("notificationCount", notificationDao.countByNotificationOfAndIsReadFalse(authenticatedUser));
		return responseJson;
	}
	
	@GetMapping("/all")
	public List<NotificationDetails> getAllNotifications(
			@RequestHeader(value="Authorization", required=false) String authorization,
			HttpServletResponse response, 
			HttpServletRequest request) {


		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();
		
		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		List<NotificationDetails> notificationDetailsList = null;
		try {
			notificationDetailsList = notificationDao.findAllProjectionBy();

		} catch (Exception e) {
			throw new RuntimeException("Couldn't get notifications");
		}


		return  notificationDetailsList;
	}
	@GetMapping("/get")
	public List<NotificationDetails> getYourNotifications(
			@RequestHeader(value="Authorization", required=false) String authorization,
			HttpServletResponse response, 
			HttpServletRequest request) {


		if(authorization == null || !jwtHandler.containsRole(authorization, "ROLE_USER"))
			throw new UnauthorizedArea();
		
		User authenticatedUser = userService.getUserByUsername(jwtHandler.getUsernameFromJWT(authorization));
		List<NotificationDetails> notificationDetailsList = null;
		try {
			notificationDetailsList = notificationService.findNotificationsOfUser(authenticatedUser);
			

		} catch (Exception e) {
			throw new RuntimeException("Couldn't get notifications");
		}


		return  notificationDetailsList;
	}
		
}
