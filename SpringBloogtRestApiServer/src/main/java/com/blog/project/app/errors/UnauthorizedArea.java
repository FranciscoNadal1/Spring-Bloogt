package com.blog.project.app.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnauthorizedArea extends RuntimeException {
	
	public UnauthorizedArea() {
		super("You are not authorized to access this area");
		
	}	
	
}