package com.blog.project.app.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoPayloadDataException extends RuntimeException {
	
	public NoPayloadDataException() {
		super("El payload está vacío");
		
	}	
	
}