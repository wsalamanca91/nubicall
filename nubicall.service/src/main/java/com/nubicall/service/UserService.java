package com.nubicall.service;

import com.nubicall.model.User;

public interface UserService {

	public User createUser(User user) throws Exception;
	
	public User deleteUser(String userName) throws Exception;
	
	public User updateUser(User user) throws Exception;
	
	public User getUser(String userName) throws Exception;
}
