package com.blog.project.app.rest.auth;

import org.springframework.beans.factory.annotation.Autowired;

import com.blog.project.app.models.service.IUserService;

import net.minidev.json.JSONObject;

public interface JWTHandler {

	public String generateJWTToken(String username, String password) throws RuntimeException;
	public boolean isTokenValid(String jwt);
	public JSONObject getJsonOfToken(String jwt);
	public boolean containsRole(String jwt, String ... role);
	public String getUsernameFromJWT(String jwt);
}
