package com.blog.project.app.rest.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/management")
public class ManagementController {

	@GetMapping("/checkStatus")
	public Map<String, String> check() {
		
		HashMap<String, String> map = new HashMap<>();
		map.put("message", "working");
		return map;

	}
}
