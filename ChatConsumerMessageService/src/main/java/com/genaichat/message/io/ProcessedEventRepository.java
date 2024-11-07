package com.genaichat.message.io;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessesedEventEntity, Long> {
	ProcessesedEventEntity findByMessageId(String msgId);
}
