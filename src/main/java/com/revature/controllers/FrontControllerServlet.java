package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;

public class FrontControllerServlet extends HttpServlet {

	private ObjectMapper objectMapper = new ObjectMapper();
	private UserAuthController uAuthController = new UserAuthController();
	private EmployeeReimbursementController eRController = new EmployeeReimbursementController();
	private FinanceManagerReimbursementController fMRController = new FinanceManagerReimbursementController();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		
		response.setStatus(404);

		final String URL = request.getRequestURI().replace("/ERS/core/", "");
		
		String[] UrlSections = URL.split("/");
		
		switch (UrlSections[0]) {
		case "register":
			if (request.getMethod().equals("POST")) {
				uAuthController.register(request, response);
			}
			break;
		case "login":
			if (request.getMethod().equals("POST")) {
				uAuthController.login(request, response);
			}
			break;
		case "logout":
			if (request.getMethod().equals("GET")) {
				uAuthController.logout(request, response);
			}
			break;
		case "reimbursement":
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute("user");
			if (session != null) {
				
				if (user.getRole().equals(Role.FINANCE_MANAGER)) {
					
					if (request.getMethod().equals("GET")) {
						fMRController.getPendingReimbursements(session, response);
					}
					else if (request.getMethod().equals("PUT")) {
						BufferedReader reader = request.getReader();
						
						StringBuilder stringBuilder = new StringBuilder();
						
						String line = reader.readLine();
						
						while(line!=null) {
							stringBuilder.append(line);
							line = reader.readLine();
						}
						
						String body = new String(stringBuilder);
						
						Reimbursement reimbursement = objectMapper.readValue(body, Reimbursement.class);
						
						fMRController.updateReimbursement(reimbursement, session, response);
					}		
				}
				
				else if (user.getRole().equals(Role.EMPLOYEE)) {
					
					if (request.getMethod().equals("GET")) {
						eRController.getOwnReimbursements(session, response);
					}
					else if (request.getMethod().equals("POST")) {
BufferedReader reader = request.getReader();
						
						StringBuilder stringBuilder = new StringBuilder();
						
						String line = reader.readLine();
						
						while(line!=null) {
							stringBuilder.append(line);
							line = reader.readLine();
						}
						
						String body = new String(stringBuilder);
						
						Reimbursement reimbursement = objectMapper.readValue(body, Reimbursement.class);
						
						eRController.addReimbursement(reimbursement, session, response);
					}
				}
				
				else {
					response.setStatus(401);
				}
				break;
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
