package com.genaichat.message.io;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, Long> {
	ProcessedEventEntity findByMessageId(String msgId);
	ProcessedEventEntity findByChatId(String chtId);
	List<ProcessedEventEntity> findByRecieverIdInOrUserIdIn(List<String> rcvrId, List<String> userId);
	List<ProcessedEventEntity> findByRecieverIdAndUserIdOrUserIdAndRecieverId(String rcvrId, String userId, String grcvrId, String guserId);
	
}
