package com.example.notification.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.notification.service.RedisService;

@Service
public class KafkaConsumerService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
	private static final int MAX_MESSAGE_SIZE = 10_485_760; // 10MB

	private final RedisService redisService;
	private final KafkaTemplate<String, String> kafkaTemplate;

	public KafkaConsumerService(RedisService redisService, KafkaTemplate<String, String> kafkaTemplate) {
		this.redisService = redisService;
		this.kafkaTemplate = kafkaTemplate;
	}

	@KafkaListener(topics = "notification-events", groupId = "notification-group")
	public void receiveMessage(@Payload String message, Acknowledgment acknowledgment) {

		try {
			// Calculate message size from payload
			int messageSize = message.getBytes().length;

			if (messageSize > MAX_MESSAGE_SIZE) {
				logger.warn("Oversized message received ({} bytes)", messageSize);
				acknowledgment.acknowledge(); // Skip to prevent re-processing
				return;
			}

			logger.debug("Processing message ({} bytes)", messageSize);
			redisService.storeNotification(message);
			acknowledgment.acknowledge();

		} catch (DataAccessException e) {
			logger.error("Redis storage failed: {}", e.getMessage());
			throw e; // Will trigger retry
		} catch (Exception e) {
			logger.error("Processing failed: {}", e.getMessage());
			throw e; // Will trigger retry
		}
	}

	@KafkaListener(topics = "notification-topic", groupId = "notification-group")
	public void consumeNotification(ConsumerRecord<String, String> record) {
		try {
			logger.info("[MAIN] Received: {}", record.value());

			// Add your business logic here (this is simulated)
			throw new RuntimeException("Simulated failure in main consumer");

		} catch (Exception ex) {
			logger.error("Error in MAIN consumer: {} — Forwarding to Retry Topic", ex.getMessage());
			kafkaTemplate.send("notification-topic.retry", record.value());
		}
	}

	/**
	 * Retry consumer that sends failed retries to DLQ
	 */
	@KafkaListener(topics = "notification-topic.retry", groupId = "notification-group")
	public void retryConsumer(ConsumerRecord<String, String> record) {
		try {
			logger.info("RETRY Received: {}", record.value());

			// Retry logic here — simulate another failure
			throw new RuntimeException("Simulated retry failure");

		} catch (Exception ex) {
			logger.error("Retry failed: {} — Sending to DLQ", ex.getMessage());
			kafkaTemplate.send("notification-topic.dlq", record.value());
		}
	}

	/**
	 * ☠️ DLQ consumer for logging or alerting
	 */
	@KafkaListener(topics = "notification-topic.dlq", groupId = "notification-group")
	public void dlqConsumer(ConsumerRecord<String, String> record) {
		logger.warn("☠️ [DLQ] Message moved to Dead Letter Queue: {}", record.value());

		// Optional: Alert, persist to DB, etc.
	}
}