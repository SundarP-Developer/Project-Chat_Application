package com.package1.ChatApplication.Configuration;

import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.package1.ChatApplication.ChatHandler.ChatHandler;

@Configuration
@EnableWebSocket
public class WebConfig implements WebSocketConfigurer{
	 
	@Autowired
    private ChatHandler chatHandler; 
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(chatHandler, "/chat").setAllowedOrigins("*");
	}
}