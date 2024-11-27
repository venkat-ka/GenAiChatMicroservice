package com.genaichat.message.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.genaichat.message.ChatAiCreatedEvent;
import com.genaichat.message.rest.CreateMessageRestModel;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public ChatMessageServiceImpl(KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate) {

		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public String createMessage(CreateMessageRestModel message) throws Exception {
		// TODO Auto-generated method stub
		String chatId = UUID.randomUUID().toString();
		LOGGER.info("*** Before publishing a productcreatedevent ****");
		// persists product in to database table before publising to event;
		ChatAiCreatedEvent chEvent = new ChatAiCreatedEvent(chatId, message.getUserId(),  message.getRecieverId(),  message.getMessage(), "");
		
		ProducerRecord<String, ChatAiCreatedEvent> recordRes = new ProducerRecord<>("genchat-created-events-topic", chatId, chEvent);
		
		
//		CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("genchat-created-events-topic", productId, prEvnt);
//		future.whenComplete((res, exc)->{
//			if(exc != null) {
//				logger.error("Failed to send message"+exc.getMessage());
//			}else {
//				logger.info("Message send successfully"+res.getRecordMetadata());
//			}
//		});
//		
//		// ths method will block currect thread to wait for result
//		// it is basically synchronous 
//		// If we remove the it will be asynchrously
//		// future.join();
		recordRes.headers().add("messageId", UUID.randomUUID().toString().getBytes());
		SendResult<String, ChatAiCreatedEvent> result = kafkaTemplate.send(recordRes).get();
		
		LOGGER.info("Partition"+result.getRecordMetadata());
		LOGGER.info("Topic"+result.getRecordMetadata().topic());
		LOGGER.info("Topic"+result.getRecordMetadata().offset());
		LOGGER.info("*** Returning ProductId ****");

		return chatId;
	}

	@Override
	public String messageByUserId(String message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
