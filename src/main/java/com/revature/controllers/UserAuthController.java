package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;
import com.revature.services.AuthService;

public class UserAuthController {
	
	private AuthService authService = new AuthService();
	private ObjectMapper mapper = new ObjectMapper();

	public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		BufferedReader reader = request.getReader();
		
		StringBuilder stringBuilder = new StringBuilder();
		
		String line = reader.readLine();
		
		while(line != null) {
			stringBuilder.append(line);
			line = reader.readLine();
		}
		
		String body = new String(stringBuilder);
		
		User user = mapper.readValue(body, User.class);
		
		User newUser = authService.register(user);
		HttpSession session = request.getSession();
		session.setAttribute("user", newUser);
		if (session.getAttribute("user") == newUser) {
			response.setStatus(201);
		}
		else {
			response.setStatus(400);
		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		BufferedReader reader = request.getReader();
		
		StringBuilder stringBuilder = new StringBuilder();
		
		String line = reader.readLine();
		
		while(line != null) {
			stringBuilder.append(line);
			line = reader.readLine();
		}
		
		String body = new String(stringBuilder);
		
		User user = mapper.readValue(body, User.class);
		
		Optional<User> authUser = authService.login(user.getUsername(), user.getPassword());
		
		if (authUser.isPresent()) {
			HttpSession session = request.getSession();
			session.setAttribute("user", authUser.get());
			response.setStatus(200);
		}
		else {
			response.setStatus(401);
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session= request.getSession();
		session.invalidate();
	}

}
