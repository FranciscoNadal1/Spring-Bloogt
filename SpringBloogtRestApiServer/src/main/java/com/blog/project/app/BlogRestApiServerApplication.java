package com.blog.project.app;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.blog.project.app.entities.User;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.scheduled.UserCreation;
import com.blog.project.app.utils.LocalUtils;

@SpringBootApplication
@EnableScheduling
public class BlogRestApiServerApplication  implements CommandLineRunner {

	@Autowired
	UserCreation userCreation;

	
	public static void main(String[] args) {
		SpringApplication.run(BlogRestApiServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		
		
	}	
}
