package com.example.notification.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.notification.security.JwtAuthenticationFilter;
import com.example.notification.security.JwtUtil;

import io.jsonwebtoken.security.Keys;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private static final String[] AUTH_WHITELIST = { "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
			"/webjars/**" };

	@Value("${url.app.jwtSecret}")
	private String jwtSecret;

	private final JwtAuthenticationFilter authFilter;
	
	public SecurityConfig(JwtAuthenticationFilter authFilter) {
		this.authFilter = authFilter;
	}

	@Bean
	public SecurityFilterChain security(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(request -> request.requestMatchers(AUTH_WHITELIST).permitAll()
						.requestMatchers("/auth/login").permitAll().anyRequest().authenticated())
				.oauth2ResourceServer(oauth -> oauth.jwt())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

}
