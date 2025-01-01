package com.genaichat.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.genaichat.chatevent.ChatAiCreatedEvent;
import com.genaichat.chatevent.PreparedMessage;



@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Value("${spring.kafka.producer.key-serializer}")
	private String keySerializer;
	
	@Value("${spring.kafka.producer.value-serializer}")
	private String valueSerializer;
	
	@Value("${spring.kafka.producer.acks}")
	private String acks;
	
//	@Value("${spring.kafka.producer.retries}")
//	private String deliveryTimeout;
	
	@Value("${spring.kafka.producer.properties.linger.ms}")
	private String linger;
	
	@Value("${spring.kafka.producer.properties.request.timeout.ms}")
	private String requestTimeout;
	
	@Value("${spring.kafka.producer.properties.enalbe.idempotence}")
	private boolean idempotence;
	
	@Value("${spring.kafka.producer.properties.max.in.flight.request.per.connection}")
	private String inflightRequest;
	
	@Autowired
	Environment environment;
	
	Map<String, Object> productConfig(){
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		config.put(ProducerConfig.ACKS_CONFIG, acks);
		
		//config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
		config.put(ProducerConfig.LINGER_MS_CONFIG, linger);
		config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
		config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,idempotence);
		config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,inflightRequest);
		//config.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE)
		return config;
	}
	
	@Bean
	ProducerFactory<String, ChatAiCreatedEvent> producerFactory(){
		return new DefaultKafkaProducerFactory<>(productConfig());
	}
	
	@Bean
	KafkaTemplate<String, ChatAiCreatedEvent> kafkaTemplate(){
		return new KafkaTemplate<String, ChatAiCreatedEvent>(producerFactory());
	}
	
	@Bean
	ProducerFactory<String, PreparedMessage> producerReadFactory(){
		return new DefaultKafkaProducerFactory<>(productConfig());
	}
	
	@Bean
	KafkaTemplate<String, PreparedMessage> kafkaReadTemplate(){
		return new KafkaTemplate<String, PreparedMessage>(producerReadFactory());
	}
	
	
	
	
	@Bean
	NewTopic createTopic() {
		return TopicBuilder.name(environment.getProperty("consumer.create.topic"))
				.partitions(3)
				.replicas(3)
				.configs(Map.of("min.insync.replicas","2"))
				.build();
	}
	
	@Bean
	NewTopic readTopic() {
		return TopicBuilder.name(environment.getProperty("consumer.read.topic"))
				.partitions(3)
				.replicas(3)
				.configs(Map.of("min.insync.replicas","2"))
				.build();
	}
	
	@Bean(name="conFact")
	ConsumerFactory<String, ChatAiCreatedEvent> consumerFactory() {

	    Map<String, Object> configProps = new HashMap<>();
	    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.producer.bootstrap-servers"));
	    configProps.put(JsonDeserializer.TRUSTED_PACKAGES,
				environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
	    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "genchat-created-events");
	    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
	    configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	    /* retrieve all message config */
//	    configProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, 
//				"read_committed");
//	    configProps.put("max.poll.interval.ms", "300000");
//	    
	    return new DefaultKafkaConsumerFactory<>(configProps);
	  }
	
	@Bean(name="kafkaConsumer")
	KafkaTemplate<String, ChatAiCreatedEvent> kafkaCTemplate(){
		KafkaTemplate<String, ChatAiCreatedEvent> kfkaTemp = new KafkaTemplate<String, ChatAiCreatedEvent>(producerFactory());
		kfkaTemp.setConsumerFactory(consumerFactory());
		return kfkaTemp;
	}
}
