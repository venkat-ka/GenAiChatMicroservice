package com.genaichat.message.service;

import java.util.List;

import com.genaichat.message.io.PreparedMessage;
import com.genaichat.message.rest.CreateMessageRestModel;

public interface ChatMessageService {
	String createMessage(CreateMessageRestModel message)throws Exception;
	String messageByUserId(String message)throws Exception;
	 List<PreparedMessage> listOfMessage(CreateMessageRestModel readMesage)throws Exception;

}
