package com.example.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
	private static final String REDIS_KEY = "notifications";

	@Autowired
	RedisTemplate<String, String> template;

	public void storeNotification(String message) {

		template.opsForList().leftPush(REDIS_KEY, message);
		logger.info("Stored notification in Redis: {}", message);

	}

}
