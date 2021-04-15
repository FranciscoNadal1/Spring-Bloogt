package com.blog.project.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.blog.project.app.auth.LoginSuccessHandler;
import com.blog.project.app.models.service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private LoginSuccessHandler successHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
    
	protected void configure(HttpSecurity http) throws Exception {
	//	http.authorizeRequests().antMatchers("/post/**", "/**").anonymous().anyRequest().authenticated();
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
		.anyRequest().permitAll()
		.and()
			.formLogin().loginPage("/login").successHandler(successHandler).permitAll()
		.and().csrf().disable()
		.logout().permitAll()
		
		;
		
	}
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception
	{	
		build.userDetailsService(userServiceImpl)
		.passwordEncoder(passwordEncoder);

	}
	

	
}
