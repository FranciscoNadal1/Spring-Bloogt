package com.blog.project.app.rest.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class IndexController {
	
	@GetMapping("/")
	public void redirectToEmbbedBlog(HttpServletResponse response) throws IOException {
		response.sendRedirect("/post/list");
	}
}
