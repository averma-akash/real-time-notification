package com.example.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notification.dto.AuthRequest;
import com.example.notification.model.User;
import com.example.notification.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	UserRepository userDao;

	public User findUser(AuthRequest request) {

		User userDtls = userDao.findByUsername(request.getUsername());
		if (null != userDtls && request.getPassword().equals(userDtls.getPassword())) {
			return userDtls;
		}
		return null;
	}

}
