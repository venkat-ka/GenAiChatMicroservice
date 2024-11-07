package com.genaichat.message.io;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="processed-event")
public class ProcessesedEventEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9031338813815992651L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false, unique=true)
	private String messageId;
	
	@Column(nullable=false)
	private String chatId;

	
	
	public ProcessesedEventEntity(String messageId, String chatId) {
		super();
		this.messageId = messageId;
		this.chatId = chatId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
}
