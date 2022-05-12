package com.revature.services;

import java.util.Optional;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 *
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {
	
	protected UserDAO userDAO = new UserDAO();
	/**
	 *     Should retrieve a User with the corresponding username or an empty optional if there is no match.
     */
	public Optional<User> getByUsername(String username) {
		
		return userDAO.getByUsername(username);
	}
	
	public User getById(int id) {
		
		return userDAO.getById(id);
	}

	public boolean createUser(String username, String password, Role role, String firstName, String lastName, String email) {
		int userId = 0;
		User user = new User(userId, username, password, role, firstName, lastName, email);
		user = userDAO.create(user);
		if (user != null) {
			return true;
		}
		return false;
	}
}
