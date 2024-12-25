package com.genaichat.message.io;

import java.io.Serializable;

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
	private Long offSetNo;
	
	@Column(nullable=true)
	private Integer partionNo;
	
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

	
}
