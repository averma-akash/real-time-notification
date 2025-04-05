package com.example.notification.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDeadLetterHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	@KafkaListener(topics = "notification-topic.dlq", groupId = "notification-dlq-group")
	public void handleDlq(ConsumerRecord<String, String> record) {

		log.info("DLQ initiated {} ", record);
		
		// You can persist data in DB 

	}

}
