package com.genaichat.message.rest;

public class CreateMessageRestModel {
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	private String userId;
	private String recieverId;
	private String message;
	private String chatId;
	private String partitionId;
	
	
	
	public CreateMessageRestModel() {
	}
	public CreateMessageRestModel(String userId, String recieverId, String message, String chatId) {
		super();
		this.userId = userId;
		this.recieverId = recieverId;
		this.message = message;
		this.chatId = chatId;
	}
	public CreateMessageRestModel(String userId, String recieverId, String message) {
		super();
		this.userId = userId;
		this.recieverId = recieverId;
		this.message = message;
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
	public String getPartitionId() {
		return partitionId;
	}
	public void setPartitionId(String partitionId) {
		this.partitionId = partitionId;
	}
	
	
}
