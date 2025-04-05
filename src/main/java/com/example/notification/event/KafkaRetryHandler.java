package com.example.notification.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRetryHandler {
	
	 private final KafkaTemplate<String, String> kafkaTemplate;
	 private Logger log = LoggerFactory.getLogger(getClass());

	public KafkaRetryHandler(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	/**
     * Handles retry logic for transient failures.
     * If it still fails, send to DLQ.
     */
	
	@KafkaListener(topics = "notification-topic.retry", groupId = "notification-retry-group")
	public void handleRetry(ConsumerRecord<String, String> record) {
		
		try {
			log.info("Retry Logic initiated {} ", record.value());
			throw new RuntimeException("Still failing after retry");
		} catch (Exception ex) {
            log.error("Retry failed — Sending to DLQ");
            kafkaTemplate.send("notification-topic.dlq", record.value());
        }
	}
	 
	 

}
