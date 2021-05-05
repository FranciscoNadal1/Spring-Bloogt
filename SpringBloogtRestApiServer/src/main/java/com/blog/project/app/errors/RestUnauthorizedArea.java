package com.blog.project.app.errors;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.blog.project.app.rest.controllers.UserController;

@ControllerAdvice
public class RestUnauthorizedArea {

	

	private static final Logger logger = LoggerFactory.getLogger(RestUnauthorizedArea.class);
    @ExceptionHandler({UnauthorizedArea.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    Map<String, String> unauthorizedAccess(Exception e) {
        Map<String, String> exception = new HashMap<String, String>();

        logger.error("unauthorized Access to the API: " + e.getMessage(), e);

        exception.put("code", "401");
        exception.put("reason", e.getMessage());

        return exception;
    }
}
