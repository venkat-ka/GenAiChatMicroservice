package com.genaichat.message.rest;

public class CreateMessageRestModel {
	private String userId;
	private String recieverId;
	private String message;
	
	
	
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
	
	
}
