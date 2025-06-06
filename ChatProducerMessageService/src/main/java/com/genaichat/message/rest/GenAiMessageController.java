package com.genaichat.message.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;

import com.genaichat.chatevent.PreparedMessage;
import com.genaichat.message.service.ChatMessageService;

@RestController
@RequestMapping("/chat")
public class GenAiMessageController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ChatMessageService chatMessageService;
	
	
	public GenAiMessageController(ChatMessageService chatMessageService) {
		
		this.chatMessageService = chatMessageService;
	}

//	@PostMapping("/")
//	public ResponseEntity<Object> createProduct(@RequestBody CreateMessageRestModel message){
//		
//		String chatId;
//		try {
//			
//			chatId = chatMessageService.createMessage(message);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			LOGGER.error(e.getMessage(), e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(), e.getMessage(), "/createMessage"));
//		}
//		
//		return ResponseEntity.status(HttpStatus.CREATED).body(chatId);
//	}
	@PostMapping("/getcheckv2")
	public String getCheck() {
		return "getPath check";
	}
	
	@PostMapping("/sendmessage")
	public ResponseEntity<Object> createProduct(@RequestBody CreateMessageRestModel message){
		
		String chatId;
		try {
			
			chatId = chatMessageService.createMessage(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(), e.getMessage(), "/createMessage"));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(chatId);
	}
	
	@PostMapping("/list")
	public ResponseEntity<Object> listProduct(@RequestBody CreateMessageRestModel message){
		
		String chatId;
		try {
			
			chatId = chatMessageService.createMessage(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(), e.getMessage(), "/createMessage"));
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(chatId);
	}
	
	@PostMapping("/readmessage")
	public List<PreparedMessage> readMessage(@RequestBody CreateMessageRestModel readMesage) {
		try {
			return chatMessageService.listOfMessage(readMesage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/consumed")
	public PreparedMessage consumerMessage(@RequestBody PreparedMessage readMesage) {
		try {
			return chatMessageService.consumedTrigger(readMesage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/remove")
	public String removeMessage(@RequestBody PreparedMessage removeMsg) {
		try {
			return chatMessageService.removeTrigger(removeMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
