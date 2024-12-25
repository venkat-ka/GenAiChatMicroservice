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
    	Long l = 3334L;
    	//registry.addEndpoint("/chat/ws-chat/info").setAllowedOrigins("http://localhost:3000").withSockJS();
    	registry.addEndpoint("/ws-chat").setAllowedOrigins("http://localhost:3000").withSockJS();
    	
    	//registry.addEndpoint("/chat/ws-chat").withSockJS();
    	//registry.addEndpoint("/chat/ws-chat/**").setAllowedOriginPatterns("*").withSockJS();

    	
    	
    	// chat client will use this to connect to the server // ws-chat config in react
		//registry.addEndpoint("/chat/ws-chat").setAllowedOrigins("http://localhost:3000").withSockJS();
    //Access-Control-Allow-Credentials
    	//registry.addEndpoint("/chat/ws-chat").setAllowedOrigins("http://localhost:3000").withSockJS();
    	//registry.addEndpoint("/chat/ws-chat").setAllowedOriginPatterns("/chat/ws-chat/**").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//    	registry.enableStompBrokerRelay("/queue", "/topic")
//        .setRelayHost("host.docker.internal");
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/topic/");
    }
}