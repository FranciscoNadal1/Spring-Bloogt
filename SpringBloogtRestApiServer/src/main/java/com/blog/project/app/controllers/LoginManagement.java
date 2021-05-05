package com.blog.project.app.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blog.project.app.utils.LocalUtils;

@Controller
public class LoginManagement {

	@Autowired
	private LocalUtils utils;

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash) {
		
		utils.addDataToMenu(model);

		model.addAttribute("titulo", "Login page");
		System.out.println(principal);
		if (principal != null) {
			flash.addFlashAttribute("info", "You are already logged in");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error",	"User or password cannot be found");
		}

		if (logout != null) {
			model.addAttribute("success", "Your session was closed successfully");
		}

		return "login";
	}
}
