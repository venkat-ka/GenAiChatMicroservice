package com.genaichat.message.rest;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.genaichat.chatevent.ChatAiCreatedEvent;

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
	public ChatAiCreatedEvent broadcastGroupMessage(@Payload ChatAiCreatedEvent message) {
		// Sending this message to all the subscribers
		return message;
	}

	@MessageMapping("/newUser")
	@SendTo("/topic/group")
	public ChatAiCreatedEvent addUser(@Payload ChatAiCreatedEvent message, SimpMessageHeaderAccessor headerAccessor) {
		// Add user in web socket session

		headerAccessor.getSessionAttributes().put("username", message.getUserId());
		return message;
	}

}
