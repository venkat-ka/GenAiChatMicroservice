package com.genaichat.chatevent;

public class ChatAiRemoveMsgEvent {
	private String chatId;
	private String userId;
	private String recieverId;
	private String messageId;
	private String message;
	private String messageType;
	
	
	
	public ChatAiRemoveMsgEvent() {
		
	}



	public ChatAiRemoveMsgEvent(String chatId, String userId, String recieverId, String message, String messageType) {
		super();
		this.chatId = chatId;
		this.userId = userId;
		this.recieverId = recieverId;
		this.message = message;
		this.messageType = messageType;
	}



	public String getMessageId() {
		return messageId;
	}



	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}



	public String getChatId() {
		return chatId;
	}



	public void setChatId(String chatId) {
		this.chatId = chatId;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public String getRecieverId() {
		return recieverId;
	}



	public void setRecieverId(String recieverId) {
		this.recieverId = recieverId;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getMessageType() {
		return messageType;
	}



	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

}
