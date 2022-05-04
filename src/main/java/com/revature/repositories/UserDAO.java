package com.revature.repositories;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {

    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
        	String sql = "SELECT * FROM ers_users WHERE ers_username = ?;";
        	
        	PreparedStatement statement = conn.prepareStatement(sql);
        
        	statement.setString(1, username);
        	
        	ResultSet result = statement.executeQuery();
        	
        	while(result.next()) {
        		User user = new User();
        		user.setId(result.getInt("ers_users_id"));
        		user.setUsername(result.getString("ers_username"));
        		user.setPassword(result.getString("ers_password"));
        		int userRoleId = result.getInt("user_role_id");
        		if (userRoleId == 1)
        		{
        			user.setRole(Role.EMPLOYEE);
        		}
        		else if (userRoleId == 2)
        		{
        			user.setRole(Role.FINANCE_MANAGER);
        		}
        		user.setFirstName(result.getString("user_first_name"));
        		user.setLastName(result.getString("user_last_name"));
        		user.setEmail(result.getString("user_email"));
        		return Optional.of(user);
        	}
        }
        catch(SQLException e) {
			e.printStackTrace();
		}
        return Optional.empty();
    }
    
    public User getById(int id) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
        	String sql = "SELECT * FROM ers_users WHERE ers_users_id = ?;";
        
        	PreparedStatement statement = conn.prepareStatement(sql);
        	
        	statement.setInt(1, id);
        	
        	ResultSet result = statement.executeQuery();
        	while(result.next()) {
        		User user = new User();
        		user.setId(result.getInt("ers_users_id"));
        		user.setUsername(result.getString("ers_username"));
        		user.setPassword(result.getString("ers_password"));
        		int userRoleId = result.getInt("user_role_id");
        		if (userRoleId == 1)
        		{
        			user.setRole(Role.EMPLOYEE);
        		}
        		else if (userRoleId == 2)
        		{
        			user.setRole(Role.FINANCE_MANAGER);
        		}
        		user.setFirstName(result.getString("user_first_name"));
        		user.setLastName(result.getString("user_last_name"));
        		user.setEmail(result.getString("user_email"));
        		return user;
        	}
        }
        catch(SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     *
     * Note: The userToBeRegistered will have an id=0, and username and password will not be null.
     * Additional fields may be null.
     */
    public User create(User userToBeRegistered) {
    	try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
    		String sql = "INSERT INTO ers_users (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES (?, ?, ?, ?, ?, ?);";
    		
    		PreparedStatement statement = conn.prepareStatement(sql);
    		
    		int count = 1;
    		
    		statement.setInt(count++, userToBeRegistered.getId());
    		statement.setString(count++, userToBeRegistered.getUsername());
    		statement.setString(count++, userToBeRegistered.getPassword());
    		statement.setString(count++, userToBeRegistered.getFirstName());
    		statement.setString(count++, userToBeRegistered.getLastName());
    		statement.setString(count++, userToBeRegistered.getEmail());
    		int userRoleId = 1;
    		if (userToBeRegistered.getRole() == Role.EMPLOYEE) {
    			userRoleId = 1;
    		}
    		else if (userToBeRegistered.getRole() == Role.FINANCE_MANAGER) {
    			userRoleId = 2;
    		}
    		statement.setInt(count, userRoleId);
    		
    		statement.execute();
    		
    		return userToBeRegistered;
    	}
    	catch(SQLException e) {
			e.printStackTrace();
		}
        return null;
    }
}
