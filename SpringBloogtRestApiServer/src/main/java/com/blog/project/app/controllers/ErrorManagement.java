package com.blog.project.app.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.project.app.utils.LocalUtils;

@Controller
public class ErrorManagement implements ErrorController  {

	@Autowired
	private LocalUtils utils;
	
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
    //	String status = (String) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    //	 Object status = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
/*
    	System.out.println("--------------------------------------");
    	String errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
    	System.out.println("errorMessage");
    	String errorException = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION).toString();
    	String errorException2 = request.getAttribute(RequestDispatcher.).toString();
    	System.out.println("errorException");
    //	String errorException = RequestDispatcher.
    //	System.out.println("errorMessage");
    	System.out.println("--------------------------------------");
*/

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