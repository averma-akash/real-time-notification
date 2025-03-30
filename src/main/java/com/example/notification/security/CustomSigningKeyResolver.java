package com.example.notification.security;

import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class CustomSigningKeyResolver extends SigningKeyResolverAdapter {

	private static String jwtSecret = "realtimenotificationservice32byt";

	public Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(jwtSecret.getBytes()));
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public Key resolveSigningKey(JwsHeader header, Claims claims) {
		return getSigningKey();
	}
}