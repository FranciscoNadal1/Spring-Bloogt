package com.blog.project.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
/*
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService customUserDetailsService;
*/	
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/post/**", "/**").anonymous().anyRequest().authenticated();
	}
	
    @Bean 
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }

	
}
