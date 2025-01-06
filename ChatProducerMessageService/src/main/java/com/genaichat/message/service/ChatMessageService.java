package com.genaichat.message.service;

import java.util.List;

import com.genaichat.chatevent.PreparedMessage;
import com.genaichat.message.rest.CreateMessageRestModel;

public interface ChatMessageService {
	String createMessage(CreateMessageRestModel message)throws Exception;
	String updateMessage(CreateMessageRestModel message)throws Exception;
	String messageByUserId(String message)throws Exception;
	 List<PreparedMessage> listOfMessage(CreateMessageRestModel readMesage)throws Exception;
	 PreparedMessage consumedTrigger(PreparedMessage readMesage)throws Exception;
	 String removeTrigger(PreparedMessage readMesage)throws Exception;

}
