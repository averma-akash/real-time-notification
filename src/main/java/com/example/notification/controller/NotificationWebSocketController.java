package com.example.notification.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notify")
public class NotificationWebSocketController {

	private final SimpMessagingTemplate simpMessagingTemplate;

	public NotificationWebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@PostMapping("/send")
	public String sendNotificationToClients(@RequestBody String message) {
		// /topic/notifications → topic clients are subscribed to
		simpMessagingTemplate.convertAndSend("/topic/notifications", message);
		return "Message sent to WebSocket clients";
	}

}
