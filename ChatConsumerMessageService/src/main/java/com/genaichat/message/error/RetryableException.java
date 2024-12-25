package com.genaichat.message.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryableException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	public RetryableException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
		LOGGER.info("Erro Not retrayable first time => {}",message);
	}

	public RetryableException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
		LOGGER.info("Erro Not retrayable first time => {}",cause);
	}
}
