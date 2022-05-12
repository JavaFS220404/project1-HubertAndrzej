package com.revature.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.ReimbursementService;

public class FinanceManagerReimbursementController {
	
	private ReimbursementService reimbursementService = new ReimbursementService();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public void getPendingReimbursements(HttpSession session, HttpServletResponse response) throws IOException {
		List<Reimbursement> reimbursements = reimbursementService.getReimbursementsByStatus(Status.PENDING);
	
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
	
	public void updateReimbursement(Reimbursement reimbursement, HttpSession session, HttpServletResponse response) throws IOException {
		User user = (User) session.getAttribute("user");
		Reimbursement updatedReimbursement = reimbursementService.process(reimbursement, reimbursement.getStatus(), user);
		
		if (updatedReimbursement == null) {
			response.setStatus(400);
		}
		else {
			response.setStatus(200);
			String json = objectMapper.writeValueAsString(reimbursement);
			PrintWriter print = response.getWriter();
			print.print(json);
		}
	}
}
