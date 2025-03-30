package com.example.notification.security;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtUtil {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	@Value("${url.app.jwtExpirationMs}")
	private long jwtExpirationMs;

	@Value("${url.app.jwtCookieName}")
	private String jwtCookie;

	private final CustomSigningKeyResolver signingKeyResolver = new CustomSigningKeyResolver();

	public String createJwtToken(String username) {
		logger.info("*** Create JWT Token ***");

		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(signingKeyResolver.getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public String addResponseCookie(HttpServletResponse response, String token) {

		logger.info("*** Set Response Cookie ***");
		
		return ResponseCookie.from(jwtCookie, token).path("/").maxAge(24 * 60 * 60).httpOnly(true)
				.build().toString();
		
//		Cookie cookie = new Cookie(jwtCookie, token);
//		cookie.setHttpOnly(true);
//		cookie.setSecure(true);
//		cookie.setPath("/");
//		cookie.setMaxAge(24 * 60 * 60);
//		cookie.setAttribute("SameSite", "Strict");
//		response.addCookie(cookie);

	}

	public String getJwtFromCookie(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	/*
	 * parseClaimsJws --> Used for signed JWTs (JWS) This method validates the
	 * signature of the JWT If the JWT is tampered with, this method throws an error
	 * 
	 * parseClaimsJwt --> Used for unsigned JWTs (JWT without a signature) This
	 * method does not verify the signature It only decodes the JWT and extracts
	 * claims without checking integrity.
	 */
	public String extractUsername(String token) {

		logger.info("*** Etracting Username ***");
		return Jwts.parserBuilder().setSigningKeyResolver(signingKeyResolver).build().parseClaimsJws(token).getBody()
				.getSubject();

	}

	public Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKeyResolver(signingKeyResolver).build().parseClaimsJws(token).getBody();
	}

	public boolean validateJwtToken(String authToken, String username) {
		try {
			return extractUsername(authToken).equals(username)
					&& getClaims(authToken).getExpiration().after(new Date());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
