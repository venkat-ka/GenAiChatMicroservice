package com.genaichat.message.service;

import com.genaichat.message.rest.CreateMessageRestModel;

public interface ChatMessageService {
	String createMessage(CreateMessageRestModel message)throws Exception;
	String messageByUserId(String message)throws Exception;
}
