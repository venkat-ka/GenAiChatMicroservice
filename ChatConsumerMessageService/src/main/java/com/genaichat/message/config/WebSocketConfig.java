package com.genaichat.message.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	 
	  //private String brokerRelayHost;
	@Autowired
	private Environment env;
	 
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {    	
    	registry.addEndpoint("/ws-chat").setAllowedOrigins("http://localhost:3000").withSockJS();    	
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/topic/");
    }
}