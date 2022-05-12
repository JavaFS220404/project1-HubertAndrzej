package com.revature.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.ReimbursementService;

public class EmployeeReimbursementController {
	
	private ReimbursementService reimbursementService = new ReimbursementService();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	
	
	public void addReimbursement(Reimbursement reimbursement, HttpSession session, HttpServletResponse response) throws IOException {
		User author = (User) session.getAttribute("user");
		Reimbursement newReimbursement = reimbursementService.create(reimbursement);
		
		if (newReimbursement != null) {
			newReimbursement.setAuthor(author);
			response.setStatus(201);
			String json = objectMapper.writeValueAsString(reimbursement);
			PrintWriter print = response.getWriter();
			print.print(json);
		} 
		else {
			response.setStatus(400);
		}
	}

	public void getOwnReimbursements(HttpSession session, HttpServletResponse response) throws IOException {
		
		User user = (User) session.getAttribute("user");
		
		List<Reimbursement> reimbursements = reimbursementService.getReimbursementsByAuthor(user);
		
		if (reimbursements.size() == 0) {
			response.setStatus(204);
		}
		else {
			response.setStatus(200);
			String json = objectMapper.writeValueAsString(reimbursements);
			PrintWriter printWriter = response.getWriter();
			printWriter.print(json);
		}
		
	}
}
