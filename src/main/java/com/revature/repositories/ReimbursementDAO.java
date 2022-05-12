package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReimbursementDAO {

    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
    public Optional<Reimbursement> getById(int id) {
    	Reimbursement reimbursement = new Reimbursement();
    	
    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?;";
    		
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		statement.setInt(1, id);
        	
        	ResultSet result = statement.executeQuery();
    	
        	while(result.next()) {
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
		return Optional.ofNullable(reimbursement);

    }

    /**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
    public List<Reimbursement> getByStatus(Status status) {
    	List<Reimbursement> reimbursementList = new ArrayList<>();

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
        	
    	}
    	catch(SQLException e) {
			e.printStackTrace();
		}
    	return reimbursementList;
    }
    
    
    
    public Reimbursement create(Reimbursement newReimbursement) {
    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		String sql = "INSERT INTO ers_reimbursements (reimb_amount, reimb_submitted, reimb_description, reimb_author, reimb_type_id) VALUES (?, ?, ?, ?, ?);";
    	
    		PreparedStatement statement = conn.prepareStatement(sql);
        	
    		int count = 0;
    		
    		statement.setDouble(++count, newReimbursement.getAmount());
    		statement.setTimestamp(++count, newReimbursement.getSubmissionDate());
    		statement.setString(++count, newReimbursement.getDescription());
    		statement.setInt(++count, newReimbursement.getAuthor().getId());
    		int typeId = (newReimbursement.getType().ordinal()) + 1;
    		statement.setInt(++count, typeId);
    		
    		statement.execute();
    		
    		return newReimbursement;
    	}
    	catch(SQLException e) {
			e.printStackTrace();
		}

		return null;
    	
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
    		String sql = "UPDATE ers_reimbursement SET reimb_resolved = ?, reimb_resolver = ?, reimb_status_id = ? WHERE reimb_id = "+unprocessedReimbursement.getId();
    	
    		PreparedStatement statement = conn.prepareStatement(sql);
    	
    		int count = 0;
    		
    		statement.setTimestamp(++count, unprocessedReimbursement.getResolutionDate());
    		statement.setInt(++count, unprocessedReimbursement.getResolver().getId());
    		int statusId = (unprocessedReimbursement.getStatus().ordinal()) + 1;
    		statement.setInt(++count, statusId);
    		
    		statement.execute();
    		
    		return unprocessedReimbursement;
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public List<Reimbursement> getByAuthor(User user) {
    	List<Reimbursement> reimbursementList = new ArrayList<>();

    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		
    		int authorId = user.getId();
    		
    		String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ?;";
    		
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		statement.setInt(1, authorId);
        	
        	ResultSet result = statement.executeQuery();
        	
        	while(result.next()) {
        		Reimbursement reimbursement = new Reimbursement();
        		reimbursement.setId(result.getInt("reimb_id"));
        		reimbursement.setAmount(result.getDouble("reimb_amount"));;
        		reimbursement.setSubmissionDate(result.getTimestamp("reimb_submitted"));
        		reimbursement.setResolutionDate(result.getTimestamp("reimb_resolved"));
        		reimbursement.setDescription(result.getString("reimb_description"));
        		reimbursement.setReceipt(result.getBlob("reimb_receipt"));
        		UserDAO userDAO = new UserDAO();
        		User author = userDAO.getById(authorId);
        		reimbursement.setAuthor(author);
        		int resolverId = result.getInt("reimb_resolver");
        		User resolver = userDAO.getById(resolverId);
        		reimbursement.setResolver(resolver);
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
        		reimbursementList.add(reimbursement);
        	}
        	
    	}
    	catch(SQLException e) {
			e.printStackTrace();
		}
    	return reimbursementList;
    }
}
