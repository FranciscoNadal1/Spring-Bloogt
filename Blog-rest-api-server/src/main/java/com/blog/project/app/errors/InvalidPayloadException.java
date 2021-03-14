package com.blog.project.app.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidPayloadException extends RuntimeException {
	
	public InvalidPayloadException() {
		super("Payload is invalid");
		
	}	
	
}