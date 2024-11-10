package com.genaichat.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.genaichat.message.ChatAiCreatedEvent;
import com.genaichat.message.error.NotRetrayableException;
import com.genaichat.message.error.RetryableException;
import com.genaichat.message.io.ProcessedEventRepository;
import com.genaichat.message.io.ProcessesedEventEntity;

import jakarta.transaction.Transactional;

@Component
@KafkaListener(topics = "genchat-created-events-topic")
public class ChatAiCreatedEventHandler {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate;

	private ProcessedEventRepository processEventRepository;

	public ChatAiCreatedEventHandler(RestTemplate restTemplate, ProcessedEventRepository processEventRepository) {
		this.restTemplate = restTemplate;
		this.processEventRepository = processEventRepository;
	}
	
	@Transactional
	@KafkaHandler
	public void handle(@Payload ChatAiCreatedEvent chatCreatedEvent, @Header("messageId") String messageId,
			@Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
		LOGGER.info("Received a new event: " + chatCreatedEvent.getMessage()+ "" + " with ProductId: "
				+ chatCreatedEvent.getChatId());

		ProcessesedEventEntity existRcrd = processEventRepository.findByMessageId(messageId);
		if(existRcrd != null) {
			LOGGER.info("Found a duplicate message Id : {}" + existRcrd.getMessageId());
		}
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
			// save unique message id in a database table;
			processEventRepository.save(new ProcessesedEventEntity(messageId, chatCreatedEvent.getChatId()));	
		} catch(DataIntegrityViolationException dexp) {
			new NotRetrayableException(dexp);
		}
		
	}

}
