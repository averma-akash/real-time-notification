package com.example.notification.event;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
	private static final String TOPIC = "notification-events";
	private static final int MAX_MESSAGE_SIZE = 10_485_760; // 10MB

	private final KafkaTemplate<String, String> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendNotification(String message) {
		if (message.getBytes().length > MAX_MESSAGE_SIZE) {
			throw new IllegalArgumentException("Message exceeds maximum size of 10MB");
		}

		CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, message);

		future.whenComplete((result, ex) -> {
			if (ex != null) {
				logger.error("Failed to send message to topic '{}'", TOPIC, ex);
			} else {
				logger.debug("Message sent successfully to partition {} at offset {}",
						result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
			}
		});
	}
}