package com.revature.services;

import java.sql.Timestamp;
import java.util.List;

import com.revature.exceptions.ReimbursementNotCreatedException;
import com.revature.exceptions.ReimbursementNotProcessedException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;

/**
 * The ReimbursementService should handle the submission, processing,
 * and retrieval of Reimbursements for the ERS application.
 *
 * {@code process} and {@code getReimbursementsByStatus} are the minimum methods required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create Reimbursement</li>
 *     <li>Update Reimbursement</li>
 *     <li>Get Reimbursements by ID</li>
 *     <li>Get Reimbursements by Author</li>
 *     <li>Get Reimbursements by Resolver</li>
 *     <li>Get All Reimbursements</li>
 * </ul>
 */
public class ReimbursementService {
	protected ReimbursementDAO reimbursementDAO = new ReimbursementDAO();

    /**
     * <ul>
     *     <li>Should ensure that the user is logged in as a Finance Manager</li>
     *     <li>Must throw exception if user is not logged in as a Finance Manager</li>
     *     <li>Should ensure that the reimbursement request exists</li>
     *     <li>Must throw exception if the reimbursement request is not found</li>
     *     <li>Should persist the updated reimbursement status with resolver information</li>
     *     <li>Must throw exception if persistence is unsuccessful</li>
     * </ul>
     *
     * Note: unprocessedReimbursement will have a status of PENDING, a non-zero ID and amount, and a non-null Author.
     * The Resolver should be null. Additional fields may be null.
     * After processing, the reimbursement will have its status changed to either APPROVED or DENIED.
     */
    public Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, User resolver) {
    	Reimbursement reimbursement = unprocessedReimbursement;
    	if (resolver.getRole() == Role.FINANCE_MANAGER) {
        		reimbursement.setResolutionDate(new Timestamp(System.currentTimeMillis()));
        		reimbursement.setResolver(resolver);
        		reimbursement.setStatus(finalStatus);
        		reimbursementDAO.update(reimbursement);
        		return reimbursement;
        	}
    	else {	
    		throw new ReimbursementNotProcessedException();
    	}
    }

    /**
     * Should retrieve all reimbursements with the correct status.
     */
    public List<Reimbursement> getReimbursementsByStatus(Status status) {
        return reimbursementDAO.getByStatus(status);
    }
    
    public Reimbursement createReimbursement(Reimbursement reimbursement) {
    	Reimbursement reimbursementToBeAdded = reimbursementDAO.create(reimbursement);
    	if (reimbursementToBeAdded != null) {
    		return reimbursementToBeAdded;
    	}
    	else {
    		throw new ReimbursementNotCreatedException("The creation of the reimbursement request failed. Please try again!");
    	}
    }
}
