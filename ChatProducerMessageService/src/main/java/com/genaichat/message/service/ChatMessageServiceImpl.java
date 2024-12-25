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
import com.genaichat.message.io.PreparedMessage;
import com.genaichat.message.io.ProcessedEventEntity;
import com.genaichat.message.io.ProcessedEventRepository;

//import com.genaichat.message.ChatAiCreatedEvent;

import com.genaichat.message.rest.CreateMessageRestModel;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

	KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate;
	
	KafkaTemplate<String, ChatAiCreatedEvent> kafkaConsumer;
	
	private ProcessedEventRepository processEventRepository;

	// private final ConsumerFactory<String, ChatAiCreatedEvent> consumerFactory;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public ChatMessageServiceImpl(KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate,
			KafkaTemplate<String, ChatAiCreatedEvent> kafkaConsumer, ProcessedEventRepository processEventRepository) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaConsumer = kafkaConsumer;
		this.processEventRepository = processEventRepository;
	}

//	public ChatMessageServiceImpl(KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate) {
//
//		this.kafkaTemplate = kafkaTemplate;
//	}

//	public ChatMessageServiceImpl(KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate,
//			ProcessedEventRepository processEventRepository,
//			ConsumerFactory<String, ChatAiCreatedEvent> consumerFactory) {
//		super();
//		this.kafkaTemplate = kafkaTemplate;
//		this.processEventRepository = processEventRepository;
//		// this.consumerFactory = consumerFactory;
//	}
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public String createMessage(CreateMessageRestModel message) throws Exception {
		// TODO Auto-generated method stub
		String chatId = UUID.randomUUID().toString();
		LOGGER.info("*** Before publishing a productcreatedevent ****");
		// persists product in to database table before publising to event;
		ChatAiCreatedEvent chEvent = new ChatAiCreatedEvent(chatId, message.getUserId(), message.getRecieverId(),
				message.getMessage(), "");

		ProducerRecord<String, ChatAiCreatedEvent> recordRes = new ProducerRecord<>("genchat-created-events-topic",
				chatId, chEvent);

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
		String msgId = UUID.randomUUID().toString();
		LOGGER.info("msq Id ==> {}", msgId);
		byte[] messageId = msgId.getBytes();
		recordRes.headers().add("messageId", messageId);
		
		SendResult<String, ChatAiCreatedEvent> result = kafkaTemplate.send(recordRes).get();
		//result.getRecordMetadata().wait(2000L);
		Thread.sleep(2000);
		ProcessedEventEntity prEvt = processEventRepository.findByMessageId(msgId);
		//ProcessedEventEntity prEvt = new ProcessedEventEntity(msgId, chatId, message.getRecieverId(), result.getRecordMetadata().offset(), result.getRecordMetadata().partition(), result.getRecordMetadata().timestamp());
//		
		//result.getRecordMetadata().
		if(prEvt != null) {
			prEvt.setOffSetNo(result.getRecordMetadata().offset());
			prEvt.setPartionNo(result.getRecordMetadata().partition());
			prEvt.setPartionNo(result.getRecordMetadata().partition());
			prEvt.setTimeStamp(result.getRecordMetadata().timestamp());
			
			processEventRepository.save(prEvt);	
		}else {
			//processEventRepository.
			//ProcessedEventEntity prEvt = processEventRepository.findByMessageId(msgId);
			ProcessedEventEntity prSEvt = new ProcessedEventEntity();
			prSEvt.setMessageId(msgId);
			prSEvt.setOffSetNo(result.getRecordMetadata().offset());
			prSEvt.setPartionNo(result.getRecordMetadata().partition());
			prSEvt.setPartionNo(result.getRecordMetadata().partition());
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

	

	@Override
	public String messageByUserId(String message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

//	public String[] partitions(String topic) {
//
//		try (Consumer<String, ChatAiCreatedEvent> consumer = consumerFactory.createConsumer()) {
//			return consumer.partitionsFor("product-created-events-topic").stream().map(pi -> "" + pi.partition())
//					.toArray(String[]::new);
//		}
//
//	}

	@Override
	public List<PreparedMessage> listOfMessage(CreateMessageRestModel readMesage) throws Exception {
		// TODO Auto-generated method stub
		/* check mysql list by querying userId and recieverId */
		List<ProcessedEventEntity> listOfRecord = processEventRepository.findByRecieverIdInOrUserIdIn(Arrays.asList(readMesage.getUserId(), readMesage.getRecieverId()), Arrays.asList(readMesage.getUserId(), readMesage.getRecieverId()));
		List<TopicPartitionOffset> makeListOfPartionRecieveMessage = new ArrayList<>();
		listOfRecord.forEach(p->{
			LOGGER.info(p.getUserId());
			LOGGER.info("offset {} ",p.getOffSetNo());
			LOGGER.info("partition no {} ",p.getPartionNo());
			TopicPartitionOffset getMessage = new TopicPartitionOffset("genchat-created-events-topic", p.getPartionNo(), p.getOffSetNo(), SeekPosition.END);
			makeListOfPartionRecieveMessage.add(getMessage);
		});
/*		TopicPartitionOffset tpf = new TopicPartitionOffset("genchat-created-events-topic", 0, 0L, SeekPosition.END);
		TopicPartitionOffset tp = new TopicPartitionOffset("genchat-created-events-topic", 1, 2L, SeekPosition.END);
		TopicPartitionOffset tpto = new TopicPartitionOffset("genchat-created-events-topic", 2, 0L, SeekPosition.END);

		TopicPartitionOffset tpo = new TopicPartitionOffset(new TopicPartition("genchat-created-events-topic", 1), 0L,
				SeekPosition.BEGINNING);
		TopicPartitionOffset tps = new TopicPartitionOffset(new TopicPartition("genchat-created-events-topic", 2), 0L,
				SeekPosition.BEGINNING);
		TopicPartitionOffset tpF = new TopicPartitionOffset(new TopicPartition("genchat-created-events-topic", 0), 0L,
				SeekPosition.TIMESTAMP);
		TopicPartitionOffset tpoF = new TopicPartitionOffset(new TopicPartition("genchat-created-events-topic", 1), 0L,
				SeekPosition.TIMESTAMP);
		TopicPartitionOffset tpsF = new TopicPartitionOffset(new TopicPartition("genchat-created-events-topic", 2), 0L,
				SeekPosition.TIMESTAMP);

		TopicPartitionOffset oTP = new TopicPartitionOffset("genchat-created-events-topic", 0, 0L);
		TopicPartitionOffset tTP = new TopicPartitionOffset("genchat-created-events-topic", 1);
		TopicPartitionOffset thTP = new TopicPartitionOffset("genchat-created-events-topic", 2);
		*/
		// public TopicPartitionOffset(String topic, int partition, SeekPosition
		// position)
//		*LOGGER.info("Offset time 2 {} ", tpto.getTopicPartition());
//		*LOGGER.info("Offset time nw 2  {} ", tpto.getOffset());
//		*LOGGER.info("Offset time nw partion getTopicPartition 0  {} ", tpf.getTopicPartition());
//		*LOGGER.info("Offset time nw partion 0  {} ", tpf.isRelativeToCurrent());
//		*LOGGER.info("Offset time nw partion 2  {} ", tpto.isRelativeToCurrent());

		// String topic, int partition, SeekPosition position
		// tp.setOffset(34L);
		//ContainerProperties mkl = new ContainerProperties("genchat-created-event-topic");
		// *tpto.getTopicPartition();
		// *List<TopicPartitionOffset> makeListOfPartion = Arrays.asList(tpf, tp, tpto);

		// setting offset
		// ConsumerFactory<String, ProductCreatedEvent> pp = kfkcong.consumerFactory();
		// pp.getListeners()
		// var message1 = kafkaTemplate.receive(, 0L);
		// var message2 = kafkaTemplate.receive(mk, 1L);

		// Long l = 2L;
		// KafkaTemplate<String, ProductCreatedEvent> Kftemplate = new
		// KafkaTemplate<>(proConfig());
		// template.setDefaultTopic("product-created-events-topic");
//makeListOfPartion
		ConsumerRecords<String, ChatAiCreatedEvent> glt = kafkaConsumer.receive(makeListOfPartionRecieveMessage);
		// ConsumerRecord<String, ProductCreatedEvent> glt =
		// kafkaTemplate.receive("product-created-events-topic", 0, 0L,
		// Duration.ofMinutes(9L));
		//int i = 0;
//		while(glt.iterator().hasNext()) {
//			Set<TopicPartition> mk = glt.partitions();
//			mk.stream().sorted((o1, o2) -> o1.)
//		}
		//glt.iterator()
		System.out.print(" total count ==>" + glt.count());
		System.out.print("<=== End count ==> ");
		//glt.

		//glt.records("product-created-events-topic");
		// String prdTitle = glt.value().getTitle();
		List<PreparedMessage> listOfTitle = new ArrayList<>();
		// listOfTitle.add(prdTitle);
		String strTur = "";
		
		// listOfTitle.add(tp.getOffset());
		for (ConsumerRecord<String, ChatAiCreatedEvent> kk : glt) {
			
			System.out.println(kk.timestamp());
			Date m = new Date(kk.timestamp());
			System.out.println("Offset => " + m);
			System.out.println(kk.key());
			System.out.println("<==>");
			System.out.println("partition number" + kk.partition());
			System.out.println(kk.value().getMessage());
			kk.key();
			strTur = kk.key().toString();
			// prdTitle =;
			
			listOfTitle.add(new PreparedMessage(kk.timestamp(), kk.value().getMessage(), kk.value().getUserId(), kk.value().getRecieverId()));
		}
		List<PreparedMessage> res = listOfTitle.stream().sorted(Comparator.comparing(PreparedMessage::getTimeStamp)).collect(Collectors.toList());
		return res;
	}

}
