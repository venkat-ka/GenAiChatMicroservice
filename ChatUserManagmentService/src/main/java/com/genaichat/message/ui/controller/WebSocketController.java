package com.genaichat.message.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genaichat.message.shared.UserDto;

@RestController
@RequestMapping("/ws-chat")
public class WebSocketController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/check")
	public String getCHeck() {
		return "validating urls";
	}

//		
//		
//		@Autowired
//		 private KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate;

	// -------------- WebSocket API ----------------
	@MessageMapping("/sendMessage")
	@SendTo("/topic/group")
	public UserDto broadcastGroupMessage(@Payload UserDto message) {
		// Sending this message to all the subscribers
		return message;
	}

	@MessageMapping("/newUser")
	@SendTo("/topic/group")
	public UserDto addUser(@Payload UserDto message, SimpMessageHeaderAccessor headerAccessor) {
		// Add user in web socket session

		headerAccessor.getSessionAttributes().put("username", message.getUserId());
		return message;
	}

}