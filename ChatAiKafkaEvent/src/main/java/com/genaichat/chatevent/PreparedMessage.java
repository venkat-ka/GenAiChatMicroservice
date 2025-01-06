package com.genaichat.chatevent;


public class PreparedMessage {
	private Long timeStamp;
	private String message;
	private String userId;
	private String recieverId;
	private String chatId;
	private String messageType;
	private String messageId;
	private Integer partitionId;
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public PreparedMessage() {
		
	}

	public PreparedMessage(Long timeStamp, String message, String userId, String recieverId, String chatId) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.userId = userId;
		this.recieverId = recieverId;
		this.chatId = chatId;
	}
	
	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

	public PreparedMessage(Long timeStamp, String message, String userId, String recieverId, String chatId, String messageType, Integer partitionId) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.userId = userId;
		this.recieverId = recieverId;
		this.chatId = chatId;
		this.messageType = messageType;
		this.partitionId = partitionId;
	}
	
	public PreparedMessage(String userId, String recieverId, String chatId, String messageType) {
		super();
		this.userId = userId;
		this.recieverId = recieverId;
		this.chatId = chatId;
		this.messageType=messageType;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
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
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
	
}
