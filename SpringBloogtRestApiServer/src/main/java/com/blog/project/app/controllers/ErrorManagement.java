package com.blog.project.app.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.project.app.utils.LocalUtils;
/*
@Controller
public class ErrorManagement implements ErrorController  {

	@Autowired
	private LocalUtils utils;
	
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {


		model.addAttribute("titulo", "An error has ocurred");	
		model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION).toString());	

		utils.addDataToMenu(model);
        return "/errors/error_500";
    	
    	
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
*/