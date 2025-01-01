package com.genaichat.message.io;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="processed_event")
public class ProcessedEventEntity implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3440954842130802404L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false, unique=true)
	private String messageId;
	
	
	@Column(nullable=false)
	private String userId;
	
	@Column(nullable=false)
	private String recieverId;
	/* this is key */
	@Column(nullable=true)
	private String chatId;
	
	@Column(nullable=true)
	private Long offSetNo;
	
	@Column(nullable=true)
	private Integer partionNo;
	
	@Column(nullable=true)
	private String messageType;
	
	@Column(nullable=true)
	private Long timeStamp;
	
	public ProcessedEventEntity() {
		super();
	}

	public ProcessedEventEntity(String messageId, String userId, String recieverId, Long offSetNo, Integer partionNo,
			Long timeStamp) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.recieverId = recieverId;
		this.offSetNo = offSetNo;
		this.partionNo = partionNo;
		this.timeStamp = timeStamp;
	}
	
	public ProcessedEventEntity(String messageId, String userId, String recieverId, Long offSetNo, Integer partionNo,
			Long timeStamp, String chatId) {
		super();
		this.messageId = messageId;
		this.userId = userId;
		this.recieverId = recieverId;
		this.offSetNo = offSetNo;
		this.partionNo = partionNo;
		this.timeStamp = timeStamp;
		this.chatId = chatId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
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

	public Long getOffSetNo() {
		return offSetNo;
	}

	public void setOffSetNo(Long offSetNo) {
		this.offSetNo = offSetNo;
	}

	public Integer getPartionNo() {
		return partionNo;
	}

	public void setPartionNo(Integer partionNo) {
		this.partionNo = partionNo;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessageType() {
		return messageType;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessedEventEntity other = (ProcessedEventEntity) obj;
		return Objects.equals(messageId, other.messageId);
	}

	
}
