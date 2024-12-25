package com.genaichat.message.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreparedMessage {
	private Long timeStamp;
	private String message;
	private String userId;
	private String recieverId;
}
