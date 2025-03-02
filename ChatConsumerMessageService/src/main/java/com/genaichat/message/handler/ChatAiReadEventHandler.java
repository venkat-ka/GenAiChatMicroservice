package com.genaichat.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import com.genaichat.message.io.ProcessedEventEntity;
import com.genaichat.message.io.ProcessedEventRepository;
import com.libmodel.modelReq.PreparedMessage;

@Component
@KafkaListener(topics = "genchat-read-events-topic", groupId = "genchat-read-events")
public class ChatAiReadEventHandler {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate;
	@Autowired
    SimpMessagingTemplate template;
	private ProcessedEventRepository processEventRepository;
	public ChatAiReadEventHandler(RestTemplate restTemplate, ProcessedEventRepository processEventRepository) {
		super();
		this.restTemplate = restTemplate;
		this.processEventRepository = processEventRepository;
	}
	
	public void listen(@Payload PreparedMessage prepareMsg, @Header("messageId") String messageId,
			@Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        LOGGER.info("sending via kafka listener..");
        template.convertAndSend("/topic/group", prepareMsg);
    }
	
	@Transactional(propagation = Propagation.NESTED)
	@KafkaHandler
	public void handle(@Payload PreparedMessage prepareMsg, @Header("messageId") String messageId,
			@Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
		ProcessedEventEntity existRcrd = processEventRepository.findByMessageId(messageId);
		if (existRcrd != null) {
			existRcrd.setMessageType("consumed");
			processEventRepository.save(existRcrd);
		}
		 template.convertAndSend("/topic/group", existRcrd);
		LOGGER.info("Triggered Received a new event: " + prepareMsg.getMessageType() + "" + " with messageId=>: "
				+ messageId);
		}
}
