package com.example.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

	@Bean
	public RedisConnectionFactory factory() {
		return new LettuceConnectionFactory("redis", 6379);
	}

//	@Bean
//	public RedisTemplate<String, String> redisTemplate() {
//		RedisTemplate<String, String> template = new RedisTemplate<>();
//		template.setConnectionFactory(factory());
//		return template;
//	}

}
