package com.example.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.dto.AuthRequest;
import com.example.notification.model.User;
import com.example.notification.security.JwtUtil;
import com.example.notification.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final JwtUtil jwtUtil;

	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Autowired
	AuthService service;

	@PostMapping(value = "/login")
	public ResponseEntity<String> login(@RequestBody AuthRequest request, HttpServletResponse response) {
		User usrDtl = service.findUser(request);
		if (null == usrDtl) {
			return ResponseEntity.badRequest().body("**** Invalid Username or Password ****");
		}
		String token = jwtUtil.createJwtToken(request.getUsername());
		String responseCookie = jwtUtil.addResponseCookie(response, token);
		return ResponseEntity.ok().header("Set-Cookie", responseCookie)
				.body("Login SuccessFul");
	}
	
	@GetMapping("/user")
    public ResponseEntity<String> getUser(HttpServletRequest request, String username) {
//	public ResponseEntity<String> getUser(@CookieValue(name = "notification", required = false) String token, String username) {
		
		String token = jwtUtil.getJwtFromCookie(request);
        if (token != null && jwtUtil.validateJwtToken(token, username)) {
            return ResponseEntity.ok("Authenticated user: " + jwtUtil.extractUsername(token));
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }

}
