package com.example.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//This class enables WebSocket support and defines the endpoint

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling using STOMP protocol
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	/*
	 What is WebSocket?
		WebSocket is a protocol that enables real-time, two-way interactive communication between a client
		 (usually browser) and server over a single, long-lived connection.
		Unlike HTTP (which is request-response based), WebSocket allows the server to push updates to the client
		 whenever new data is available — ideal for live notifications, chat apps, or dashboards.
	 */
	
	// Register WebSocket endpoint that clients will use to connect
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-notification") // Client connects to this endpoint
		.setAllowedOriginPatterns("*")// Allow cross-origin for dev/testing
		.withSockJS();// Fallback option for browsers not supporting native WebSocket
	}
	
	// Configure message broker for routing messages between client & server
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic"); // Enables in-memory broker for subscriptions
		registry.setApplicationDestinationPrefixes("/app"); // Prefix for client messages sent to controllers
	}

}
