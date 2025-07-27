package com.package1.ChatApplication.Configuration;

import org.springframework.context.annotation.Bean;	
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {
	

	@Bean
	public SecurityFilterChain secutiryFilterChain (HttpSecurity http) throws Exception {
		
		http.csrf(cs -> cs.disable())
		     .authorizeHttpRequests(auth -> 
		      auth.requestMatchers("/auth/login","/auth/signup","/chat.html","/messages/{username}","/chat","/index.html","/signup.html").permitAll()
		     .anyRequest().authenticated()
		     )
		     .httpBasic();
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoding() {
		return new BCryptPasswordEncoder();
	}
}
