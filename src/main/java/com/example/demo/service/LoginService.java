package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

	
	@Value("${trading.username}")
	private String validUserName;
	
	@Value("${trading.password}")
	private String validPassword;
	
	public LoginService() {
		System.out.println("LoginService class no arg constructor");
	}

	public boolean isValidUser(String userName, String password) {
		return ((userName.equalsIgnoreCase(validUserName)) && (password.equalsIgnoreCase(validPassword)));
	}
}
