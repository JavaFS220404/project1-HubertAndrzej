package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReimbursementDAO {

    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
    public Optional<Reimbursement> getById(int id) {
    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?;";
    		
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		statement.setInt(1, id);
        	
        	ResultSet result = statement.executeQuery();
    	
        	while(result.next()) {
        		Reimbursement reimbursement = new Reimbursement();
        		reimbursement.setId(result.getInt("reimb_id"));
        		reimbursement.setAmount(result.getDouble("reimb_amount"));;
        		reimbursement.setSubmissionDate(result.getTimestamp("reimb_submitted"));
        		reimbursement.setResolutionDate(result.getTimestamp("reimb_resolved"));
        		reimbursement.setDescription(result.getString("reimb_description"));
        		reimbursement.setReceipt(result.getBlob("reimb_receipt"));
        		User user = new User();
        		UserDAO userDAO = new UserDAO();
        		int authorId = result.getInt("reimb_author");
        		user = userDAO.getById(authorId);
        		reimbursement.setAuthor(user);
        		int resolverId = result.getInt("reimb_resolver");
        		user = userDAO.getById(resolverId);
        		reimbursement.setResolver(user);
        		int statusId = result.getInt("reimb_status_id");
        		if (statusId == 1)
        		{
        			reimbursement.setStatus(Status.PENDING);
        		}
        		else if (statusId == 2)
        		{
        			reimbursement.setStatus(Status.APPROVED);
        		}
        		else if (statusId == 3)
        		{
        			reimbursement.setStatus(Status.DENIED);
        		}
        		int typeId = result.getInt("reimb_type_id");
        		if (typeId == 1)
        		{
        			reimbursement.setType(Type.LODGING);
        		}
        		else if (typeId == 2)
        		{
        			reimbursement.setType(Type.TRAVEL);
        		}
        		else if (typeId == 3)
        		{
        			reimbursement.setType(Type.FOOD);
        		}
        		else if (typeId == 4)
        		{
        			reimbursement.setType(Type.OTHER);
        		}
        	}
    	}
    	catch(SQLException e) {
			e.printStackTrace();
		}
    	return Optional.empty();
    }

    /**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
    public List<Reimbursement> getByStatus(Status status) {
    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		
    		int statusId = 0;
    		
    		if (status == Status.PENDING) {
    			statusId = 1;
    		}
    		else if (status == Status.APPROVED) {
    			statusId = 2;
    		}
    		else if (status == Status.DENIED)
    		{
    			statusId = 3;
    		}
    		
    		String sql = "SELECT * FROM ers_reimbursement WHERE reimb_status_id = ?;";
    		
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		statement.setInt(1, statusId);
        	
        	ResultSet result = statement.executeQuery();
        	
        	List<Reimbursement> reimbursementList = new ArrayList<>();
        	
        	while(result.next()) {
        		Reimbursement reimbursement = new Reimbursement();
        		reimbursement.setId(result.getInt("reimb_id"));
        		reimbursement.setAmount(result.getDouble("reimb_amount"));;
        		reimbursement.setSubmissionDate(result.getTimestamp("reimb_submitted"));
        		reimbursement.setResolutionDate(result.getTimestamp("reimb_resolved"));
        		reimbursement.setDescription(result.getString("reimb_description"));
        		reimbursement.setReceipt(result.getBlob("reimb_receipt"));
        		UserDAO userDAO = new UserDAO();
        		User user = new User();
        		int authorId = result.getInt("reimb_author");
        		user = userDAO.getById(authorId);
        		reimbursement.setAuthor(user);
        		int resolverId = result.getInt("reimb_resolver");
        		user = userDAO.getById(resolverId);
        		reimbursement.setResolver(user);
        		if (statusId == 1)
        		{
        			reimbursement.setStatus(Status.PENDING);
        		}
        		else if (statusId == 2)
        		{
        			reimbursement.setStatus(Status.APPROVED);
        		}
        		else if (statusId == 3)
        		{
        			reimbursement.setStatus(Status.DENIED);
        		}
        		int typeId = result.getInt("reimb_type_id");
        		if (typeId == 1)
        		{
        			reimbursement.setType(Type.LODGING);
        		}
        		else if (typeId == 2)
        		{
        			reimbursement.setType(Type.TRAVEL);
        		}
        		else if (typeId == 3)
        		{
        			reimbursement.setType(Type.FOOD);
        		}
        		else if (typeId == 4)
        		{
        			reimbursement.setType(Type.OTHER);
        		}
        		reimbursementList.add(reimbursement);
        	}
        	
        	return reimbursementList;
        	
    	}
    	catch(SQLException e) {
			e.printStackTrace();
		}
        return Collections.emptyList();
    }

    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		String sql = "UPDATE ers_reimbursement SET reimb_id = ?, reimb_amount = ?, reimb_submitted = ?, reimb_resolved = ?, reimb_description = ?, reimb_receipt = ?, reimb_author = ?, reimb_resolver = ?, reimb_status_id = ?, reimb_type_id = ? WHERE reimb_id = ?";
    	
    		PreparedStatement statement = conn.prepareStatement(sql);
    	
    		int count = 0;
    		
    		statement.setInt(++count, unprocessedReimbursement.getId());
    		statement.setDouble(++count, unprocessedReimbursement.getAmount());
    		statement.setTimestamp(++count, unprocessedReimbursement.getSubmissionDate());
    		statement.setTimestamp(++count, unprocessedReimbursement.getResolutionDate());
    		statement.setString(++count, unprocessedReimbursement.getDescription());
    		statement.setBlob(++count, unprocessedReimbursement.getReceipt());
    		statement.setInt(++count, unprocessedReimbursement.getAuthor().getId());
    		statement.setInt(++count, unprocessedReimbursement.getResolver().getId());
    		int statusId = (unprocessedReimbursement.getStatus().ordinal()) + 1;
    		statement.setInt(++count, statusId);
    		int typeId = (unprocessedReimbursement.getType().ordinal()) + 1;
    		statement.setInt(++count, typeId);
    		
    		statement.execute();
    		
    		return unprocessedReimbursement;
    		
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
}
