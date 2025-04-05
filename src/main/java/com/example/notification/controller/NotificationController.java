package com.example.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.event.KafkaProducerService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
	private final KafkaProducerService kafkaProducerService;

	@Autowired
	public NotificationController(KafkaProducerService kafkaProducerService) {
		this.kafkaProducerService = kafkaProducerService;
	}

	/**
	 * Send notification message to Kafka
	 * 
	 * @param message Notification content (required)
	 * @return ResponseEntity with operation status
	 */
	@PostMapping("/send")
	public ResponseEntity<String> sendNotification(@RequestParam(required = true) String message) {

		try {
			// Validate input
			if (!StringUtils.hasText(message)) {
				logger.warn("Attempt to send empty message");
				return ResponseEntity.badRequest().body("Message cannot be empty");
			}

			if (message.length() > 1000) { // Adjust based on your requirements
				logger.warn("Message exceeds maximum length");
				return ResponseEntity.badRequest().body("Message too long. Max 1000 characters");
			}

			logger.info("Received request to send notification");
			kafkaProducerService.sendNotification(message);

			return ResponseEntity.ok("Notification accepted for processing: " + message);

		} catch (Exception e) {
			logger.error("Failed to process notification request", e);
			return ResponseEntity.internalServerError().body("Failed to process notification. Please try again.");
		}
	}
}