package com.genaichat.message.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Comparator;
import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.kafka.support.TopicPartitionOffset.SeekPosition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.genaichat.chatevent.ChatAiCreatedEvent;
import com.genaichat.chatevent.ChatAiRemoveMsgEvent;
import com.genaichat.chatevent.PreparedMessage;
import com.genaichat.message.io.ProcessedEventEntity;
import com.genaichat.message.io.ProcessedEventRepository;

//import com.genaichat.message.ChatAiCreatedEvent;

import com.genaichat.message.rest.CreateMessageRestModel;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate;

	KafkaTemplate<String, PreparedMessage> kafkaReadTemplate;

	KafkaTemplate<String, ChatAiCreatedEvent> kafkaConsumer;

	private ProcessedEventRepository processEventRepository;

	@Autowired
	Environment env;
	// private final ConsumerFactory<String, ChatAiCreatedEvent> consumerFactory;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

//	public ChatMessageServiceImpl(KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate,
//			KafkaTemplate<String, ChatAiCreatedEvent> kafkaConsumer, ProcessedEventRepository processEventRepository) {
//		super();
//		this.kafkaTemplate = kafkaTemplate;
//		this.kafkaConsumer = kafkaConsumer;
//		this.processEventRepository = processEventRepository;
//	}

	public ChatMessageServiceImpl(KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate,
			KafkaTemplate<String, PreparedMessage> kafkaReadTemplate,
			KafkaTemplate<String, ChatAiCreatedEvent> kafkaConsumer, ProcessedEventRepository processEventRepository) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaReadTemplate = kafkaReadTemplate;
		this.kafkaConsumer = kafkaConsumer;
		this.processEventRepository = processEventRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String createMessage(CreateMessageRestModel message) throws Exception {
		// TODO Auto-generated method stub
		String chatId = UUID.randomUUID().toString();
		LOGGER.info("*** Before publishing a productcreatedevent ****");
		// persists product in to database table before publising to event;
		String msgId = UUID.randomUUID().toString();
		LOGGER.info("msq Id ==> {}", msgId);
		byte[] messageId = msgId.getBytes();
		long currentTimeMillis = System.currentTimeMillis();
		ChatAiCreatedEvent chEvent = new ChatAiCreatedEvent(chatId, message.getUserId(), message.getRecieverId(),
				message.getMessage(), "", currentTimeMillis);
		chEvent.setMessageId(msgId);
		ProducerRecord<String, ChatAiCreatedEvent> recordRes = new ProducerRecord<>(
				env.getProperty("consumer.create.topic"), chatId, chEvent);

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

		recordRes.headers().add("messageId", messageId);

		SendResult<String, ChatAiCreatedEvent> result = kafkaTemplate.send(recordRes).get();
		// result.getRecordMetadata().wait(2000L);
		Thread.sleep(2000);
		ProcessedEventEntity prEvt = processEventRepository.findByMessageId(msgId);
		// Study how to get successfull consumed message in kafka itself
		// far now it record by MYSQL
		if (prEvt != null) {

//			prEvt.setOffSetNo(result.getRecordMetadata().offset());
//			prEvt.setPartionNo(result.getRecordMetadata().partition());
//			prEvt.setPartionNo(result.getRecordMetadata().partition());
//			prEvt.setTimeStamp(result.getRecordMetadata().timestamp());

			recievedMessage(chEvent);
			// processEventRepository.save(prEvt);
		} else {

			ProcessedEventEntity prSEvt = new ProcessedEventEntity();
			prSEvt.setMessageId(msgId);
			prSEvt.setOffSetNo(result.getRecordMetadata().offset());
			prSEvt.setPartionNo(result.getRecordMetadata().partition());
			// prSEvt.setChatId(result.getProducerRecord().value().getChatId());
			prSEvt.setTimeStamp(result.getRecordMetadata().timestamp());
			prSEvt.setRecieverId(message.getRecieverId());
			prSEvt.setUserId(message.getUserId());
			processEventRepository.save(prSEvt);
		}

		LOGGER.info("Partition ==> " + result.getRecordMetadata().partition());
		LOGGER.info("Topic ==> " + result.getRecordMetadata().topic());
		LOGGER.info("Title ==> " + result.getProducerRecord().value().getMessage());
		LOGGER.info("Topic Offset  ==> " + result.getRecordMetadata().offset());
		LOGGER.info("*** Returning ProductId ****");

		return chatId;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String updateMessage(CreateMessageRestModel message) throws Exception {
		// TODO Auto-generated method stub
		String chatId = message.getChatId().toString();
		LOGGER.info("*** Before publishing a consumer ****");
		// persists product in to database table before publising to event;
		String msgId = UUID.randomUUID().toString();
		LOGGER.info("msq Id ==> {}", msgId);
		byte[] messageId = msgId.getBytes();

		ChatAiCreatedEvent chEvent = new ChatAiCreatedEvent(chatId, message.getUserId(), message.getRecieverId(),
				message.getMessage(), "consumed", message.getTimeStamp());
		chEvent.setMessageId(msgId);
		ProducerRecord<String, ChatAiCreatedEvent> recordRes = new ProducerRecord<>(
				env.getProperty("consumer.create.topic"), chatId, chEvent);

		// LOGGER.info("Checking the updated info in kafka",recordRes);
		recordRes.headers().add("messageId", messageId);

		SendResult<String, ChatAiCreatedEvent> result = kafkaTemplate.send(recordRes).get();
		// result.getRecordMetadata().wait(2000L);
		Thread.sleep(2000);
		ProcessedEventEntity prEvt = processEventRepository.findByChatId(chatId);

		if (prEvt != null) {
			prEvt.setOffSetNo(result.getRecordMetadata().offset());
			prEvt.setPartionNo(result.getRecordMetadata().partition());
			// prEvt.setPartionNo(result.getRecordMetadata().partition());
			prEvt.setTimeStamp(result.getRecordMetadata().timestamp());

			processEventRepository.save(prEvt);
		}

		LOGGER.info("Up Partition ==> " + result.getRecordMetadata().partition());
		LOGGER.info("Up Topic ==> " + result.getRecordMetadata().topic());
		LOGGER.info("Up Title ==> " + result.getProducerRecord().value().getMessage());
		LOGGER.info("Up Topic Offset  ==> " + result.getRecordMetadata().offset());
		LOGGER.info("Up Chat Id  ==> " + result.getProducerRecord().value().getChatId());
		LOGGER.info("Up message Id  ==> " + result.getProducerRecord().value().getMessageId());
		LOGGER.info("Up message Type  ==> " + result.getProducerRecord().value().getMessageType());
		LOGGER.info("*** Up Returning ProductId ****");

		return chatId;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public String messageByUserId(String message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PreparedMessage> listOfMessage(CreateMessageRestModel readMesage) throws Exception {
		// TODO Auto-generated method stub
		/* check mysql list by querying userId and recieverId */
		List<ProcessedEventEntity> listOfRecord = processEventRepository.findByRecieverIdAndUserIdOrUserIdAndRecieverId(
				readMesage.getRecieverId(), readMesage.getUserId(), readMesage.getRecieverId(), readMesage.getUserId());
		List<TopicPartitionOffset> makeListOfPartionRecieveMessage = new ArrayList<>();
		listOfRecord.forEach(p -> {
			LOGGER.info(p.getUserId());
			LOGGER.info("offset {} ", p.getOffSetNo());
			LOGGER.info("partition no {} ", p.getPartionNo());
			TopicPartitionOffset getMessage = new TopicPartitionOffset("genchat-created-events-topic", p.getPartionNo(),
					p.getOffSetNo(), SeekPosition.END);
			makeListOfPartionRecieveMessage.add(getMessage);
		});

//makeListOfPartion
		ConsumerRecords<String, ChatAiCreatedEvent> glt = kafkaConsumer.receive(makeListOfPartionRecieveMessage);

		List<PreparedMessage> listOfTitle = new ArrayList<>();

		String strTur = "";

		for (ConsumerRecord<String, ChatAiCreatedEvent> kk : glt) {

			System.out.println(kk.timestamp());
			Date m = new Date(kk.timestamp());
			System.out.println("Offset => " + m);
			System.out.println(kk.key());
			System.out.println("<==>");
			System.out.println("partition number" + kk.partition());
			if (kk.value() != null) {
				System.out.println(kk.value().getMessage());
				System.out.println("message Type ==>" + kk.value().getMessageType());
				System.out.println("message id ==>" + kk.value().getMessageId());
				if (kk.value().getTimeStamp() != null) {
					listOfTitle.add(new PreparedMessage(kk.value().getTimeStamp(), kk.value().getMessage(),
							kk.value().getUserId(), kk.value().getRecieverId(), kk.value().getChatId(),
							kk.value().getMessageType(), kk.partition()));
				}
			} else {
				if (kk.value().getTimeStamp() != null) {
					// Long timeStamp, String message, String userId, String recieverId, String
					// chatId, String messageType, Integer partitionId
					listOfTitle.add(new PreparedMessage(kk.value().getTimeStamp(), null, null, null, null, null,
							kk.partition()));
				}
			}
			strTur = kk.key().toString();
			// String messageType =
			// listOfRecord.stream().filter(usrDt->usrDt.getMessageId().equalsIgnoreCase(kk.value().getMessageId())).map(p->p.getMessageType()).findFirst().orElseGet(()->"no
			// data");
			// .findFirst().map(p->p.getMessageId()).orElseGet(()->"");
			// System.out
			// System.out.println("message Type Target result ==>" +messageType);
			// listOfTitle.add(new PreparedMessage(kk.timestamp(), kk.value().getMessage(),
			// kk.value().getUserId(), kk.value().getRecieverId(), kk.value().getChatId(),
			// kk.value().getMessageType(), kk.partition()));
		}
		List<PreparedMessage> res = listOfTitle.stream().sorted(Comparator.comparing(PreparedMessage::getTimeStamp))
				.collect(Collectors.toList());

		return res;
	}

	@Override
	public PreparedMessage consumedTrigger(PreparedMessage readMsg) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		LOGGER.info("*** Before Trigger a productreadevent ****");
		// persists product in to database table before publising to event;
		// readMsg.getChatId(), readMsg.getUserId(), readMsg.getRecieverId()
		PreparedMessage chEvent = new PreparedMessage(readMsg.getUserId(), readMsg.getRecieverId(), readMsg.getChatId(),
				"consumed");

		CreateMessageRestModel evnt = new CreateMessageRestModel(readMsg.getUserId(), readMsg.getRecieverId(),
				readMsg.getMessage(), readMsg.getChatId(), readMsg.getTimeStamp());
//				ProducerRecord<String, PreparedMessage> recordRes = new ProducerRecord<>(env.getProperty("consumer.read.topic"),
//						readMsg.getChatId(), chEvent);

//				CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("genchat-created-events-topic", productId, prEvnt);
//				future.whenComplete((res, exc)->{
//					if(exc != null) {
//						logger.error("Failed to send message"+exc.getMessage());
//					}else {
//						logger.info("Message send successfully"+res.getRecordMetadata());
//					}
//				});
//				
//				// ths method will block currect thread to wait for result
//				// it is basically synchronous 
//				// If we remove the it will be asynchrously
//				// future.join();

		String msgId = readMsg.getMessageId();
		LOGGER.info("consumer trigger msq Id ==> {}", msgId);
		LOGGER.info("consumer trigger  msq da ==> {}", readMsg.getMessage());

		// recordRes.headers().add("messageId", messageId);

		// SendResult<String, PreparedMessage> result =
		// kafkaReadTemplate.send(recordRes).get();

		// CreateMessageRestModel upd = new
		// CreateMessageRestModel(result.getProducerRecord().value().getUserId(),
		// result.getProducerRecord().value().getRecieverId(),
		// result.getProducerRecord().value().getMessage(),
		// result.getProducerRecord().value().getChatId());
		updateMessage(evnt);
//				LOGGER.info("Partition ==> " + result.getRecordMetadata().partition());
//				LOGGER.info("Topic ==> " + result.getRecordMetadata().topic());
//				LOGGER.info("msgtype ==> " + result.getProducerRecord().value().getMessageType());
//				LOGGER.info("Topic Offset  ==> " + result.getRecordMetadata().offset());
//				LOGGER.info("*** Returning ProductId ****");

		return chEvent;
	}

	@Override
	public String removeTrigger(PreparedMessage readMesage) throws Exception {

		ProducerRecord<String, ChatAiRemoveMsgEvent> recordRes = new ProducerRecord<>(
				env.getProperty("consumer.create.topic"), readMesage.getChatId(), null);
		String msgId = UUID.randomUUID().toString();

		byte[] messageId = msgId.getBytes();
		recordRes.headers().add("removeId", messageId);
		// recordRes.headers().add("PartitionId",
		// readMesage.getPartitionId().getBytes());
		// String topic, Integer partition, K key, V data
		ChatAiCreatedEvent upValue = new ChatAiCreatedEvent(readMesage.getChatId(), readMesage.getUserId(),
				readMesage.getUserId(), null, null, readMesage.getTimeStamp());
		SendResult<String, ChatAiCreatedEvent> result = kafkaTemplate.send(env.getProperty("consumer.create.topic"),
				readMesage.getPartitionId(), readMesage.getChatId(), upValue).get();
		// SendResult<String, ChatAiCreatedEvent> result =
		// kafkaTemplate.send(recordRes).get();

		ProcessedEventEntity prEvt = processEventRepository.findByChatId(readMesage.getChatId());
		if (prEvt != null) {
			prEvt.setPartionNo(result.getRecordMetadata().partition());
			prEvt.setOffSetNo(result.getRecordMetadata().offset());
			processEventRepository.save(prEvt);
		}
		return readMesage.getChatId();
	}

	@Override
	public String recievedMessage(ChatAiCreatedEvent message) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String chatId = message.getChatId().toString();

		// persists product in to database table before publising to event;
		String msgId = UUID.randomUUID().toString();

		byte[] messageId = msgId.getBytes();

//				ChatAiCreatedEvent chEvent = new ChatAiCreatedEvent(chatId, message.getUserId(), message.getRecieverId(),
//						message.getMessage(), "recieved");
		message.setMessageId(msgId);
		message.setMessageType("received");
		ProducerRecord<String, ChatAiCreatedEvent> recordRes = new ProducerRecord<>(
				env.getProperty("consumer.create.topic"), chatId, message);

		// LOGGER.info("Checking the updated info in kafka",recordRes);
		recordRes.headers().add("messageId", messageId);

		SendResult<String, ChatAiCreatedEvent> result = kafkaTemplate.send(recordRes).get();
		// result.getRecordMetadata().wait(2000L);
		Thread.sleep(2000);
		ProcessedEventEntity prEvt = processEventRepository.findByChatId(chatId);

		if (prEvt != null) {
			prEvt.setOffSetNo(result.getRecordMetadata().offset());
			prEvt.setPartionNo(result.getRecordMetadata().partition());
			prEvt.setPartionNo(result.getRecordMetadata().partition());
			prEvt.setTimeStamp(result.getRecordMetadata().timestamp());

			processEventRepository.save(prEvt);
		}

		return chatId;
	}

}
