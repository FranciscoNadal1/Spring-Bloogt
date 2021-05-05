package com.blog.project.app.rest.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.errors.UnauthorizedArea;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.rest.auth.JWTHandler;
import com.blog.project.app.utils.LocalUtils;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoginRestController {

	private String contentType = "application/json";
	private static final Logger logger = LoggerFactory.getLogger(LoginRestController.class);
	
	@Autowired
	private JWTHandler jwtHandler;
	

	@Autowired
	private IUserService userService;
	
	@GetMapping("/login")
	public List<UserData> getAllUsers(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<UserData> returningJSON = userService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}


	@PostMapping("/login")
	public JSONObject login(@RequestBody Map<String, Object> payload, HttpServletResponse response) throws UnsupportedEncodingException {

		String username = (String) payload.get("username");
		String password = (String) payload.get("password");
		
		String token = jwtHandler.generateJWTToken(username, password);

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User and password are correct, token generated");
		responseJson.appendField("generatedToken", token);
		
		/*
		Cookie cookie = new Cookie( "token", URLEncoder.encode( token, "UTF-8" ) );
		response.addCookie(cookie);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		*/
		
		return responseJson;		
	}
	
	@PostMapping("/validateToken")
	public JSONObject validateToken(@RequestBody Map<String, Object> payload) {

		String token = (String) payload.get("token");
		
		
		//String token = jwtHandler.generateJWTToken(username, password);

		JSONObject responseJson = new JSONObject();
		if(jwtHandler.isTokenValid(token)) {

			responseJson.appendField("message", "Token is valid");
			responseJson.appendField("status", "OK");
		}
		else {
			logger.info(token);
			responseJson.appendField("message", "Token is not valid or expired");
			responseJson.appendField("status", "KO");
		}
			

		responseJson.appendField("generatedToken", token);
		
		return responseJson;		
	}	
	/*
	@PostMapping("/decodeToken")
	public JSONObject decodeToken(@RequestBody Map<String, Object> payload) {
		String token = (String) payload.get("token");
		if(jwtHandler.containsRole(token, "ROLE_ADMIN", "ROLE_USER"))
			throw new RuntimeException("Forbidden");
		
		
		//String token = jwtHandler.generateJWTToken(username, password);

		JSONObject responseJson = new JSONObject();
		if(jwtHandler.isTokenValid(token)) {

			responseJson.appendField("message", "Token is valid");
			responseJson.appendField("status", "OK");
		}
		else {
			logger.info(token);
			responseJson.appendField("message", "Token is not valid or expired");
			responseJson.appendField("status", "KO");
		}
		
		

		responseJson.appendField("generatedToken", token);
		
		return jwtHandler.getJsonOfToken(token);	
	}
	*/
	@PostMapping("/checkRole")
	public JSONObject checkRole(@RequestBody Map<String, Object> payload, @RequestHeader("Authorization") String authorization) {
		JSONObject responseJson = new JSONObject();

		//String token = (String) payload.get("token");
		
		
		if(!jwtHandler.containsRole(authorization, "ROLE_ADMIN"))
			throw new UnauthorizedArea();

			
			
			responseJson.appendField("status", "You are ADMIN");
			
			
		return responseJson;	
		
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}
