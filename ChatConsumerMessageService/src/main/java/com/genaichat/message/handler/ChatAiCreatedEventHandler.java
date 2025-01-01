package com.genaichat.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.genaichat.chatevent.ChatAiCreatedEvent;
import com.genaichat.message.error.NotRetrayableException;
import com.genaichat.message.error.RetryableException;
import com.genaichat.message.io.ProcessedEventRepository;


import com.genaichat.message.io.ProcessedEventEntity;


@Component
@KafkaListener(topics = "genchat-created-events-topic", groupId = "genchat-created-events")
public class ChatAiCreatedEventHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate;
	
	@Autowired
    SimpMessagingTemplate template;

	private ProcessedEventRepository processEventRepository;

	public ChatAiCreatedEventHandler(RestTemplate restTemplate, ProcessedEventRepository processEventRepository) {
		this.restTemplate = restTemplate;
		this.processEventRepository = processEventRepository;
	}

	public void listen(@Payload ChatAiCreatedEvent chatCreatedEvent, @Header("messageId") String messageId,
			@Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        LOGGER.info("sending via kafka listener..");
        
        template.convertAndSend("/topic/group", chatCreatedEvent);
    }
	
	@Transactional(propagation = Propagation.NESTED)
	@KafkaHandler
	public void handle(@Payload ChatAiCreatedEvent chatCreatedEvent, @Header("messageId") String messageId,
			@Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
		LOGGER.info("Received a new event: " + chatCreatedEvent.getMessage() + "" + " with messageId: "
				+ chatCreatedEvent.getChatId());

		LOGGER.info(" with message key: "
				+ messageKey);
		
		String requestUrl = "http://localhost:8087/response/200";

		try {
			ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

			if (response.getStatusCode().value() == HttpStatus.OK.value()) {
				LOGGER.info("Received response from a remote service: " + response.getBody());
			}
		} catch (ResourceAccessException ex) {
			LOGGER.error(ex.getMessage());
			throw new RetryableException(ex);
		} catch (HttpServerErrorException ex) {
			LOGGER.error(ex.getMessage());
			throw new NotRetrayableException(ex);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new NotRetrayableException(ex);
		}

		try {
			// String messageId, String userId, String recieverId, long offSetNo,
			// Integer partionNo, Long timeStamp
			// save unique message id in a database table; 
			ProcessedEventEntity existRcrd = processEventRepository.findByMessageId(messageId);
			LOGGER.info("ExistRcrd details => {}", existRcrd);
			ProcessedEventEntity existChrtId = processEventRepository.findByChatId(chatCreatedEvent.getChatId());
			
			//LOGGER.info("Sending broadcasting the message started" + existRcrd.getMessageId());
			//template.convertAndSend("/topic/group", chatCreatedEvent);
			//LOGGER.info("Sending broadcasting the message completed" + existRcrd.getMessageId());
			if (existRcrd == null && existChrtId == null) {
				
				if(existChrtId == null) {
					chatCreatedEvent.setMessageType("received");
					chatCreatedEvent.setChatId(chatCreatedEvent.getChatId());
					template.convertAndSend("/topic/group", chatCreatedEvent);
				}
				
				processEventRepository.save(new ProcessedEventEntity(messageId, chatCreatedEvent.getUserId(),
						chatCreatedEvent.getRecieverId(), null, null, null, null, chatCreatedEvent.getChatId()));
				
			}
			else if(existChrtId != null && existChrtId.getChatId().equalsIgnoreCase(chatCreatedEvent.getChatId()))
			{
				//existRcrd.setMessageType("consumed");
				
				ProcessedEventEntity nExRcrd = processEventRepository.findByChatId(existChrtId.getChatId());
				LOGGER.info("Found a duplicate message Id : {}" + nExRcrd.getMessageId());
				nExRcrd.setMessageType("consumed");
				
				processEventRepository.save(nExRcrd);
				
				template.convertAndSend("/topic/group", nExRcrd);
				
				
			}else {
				
			}
			//chatCreatedEvent.setMessageId(messageId);
			
			
			
		} catch (DataIntegrityViolationException dexp) {
			new NotRetrayableException(dexp);
		}

	}

}
