package com.genaichat.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChatKafkaDemoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatKafkaDemoServiceApplication.class, args);
	}

}
