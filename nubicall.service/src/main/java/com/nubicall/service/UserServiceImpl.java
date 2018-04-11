package com.nubicall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nubicall.model.User;
import com.nubicall.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository; 
	
	@Override
	@Transactional
	public User createUser(User user) throws Exception{
		if(user == null){
			return user;
		}
		user = userRepository.saveAndFlush(user);
		return user;
	}

	@Override
	@Transactional
	public User deleteUser(String userName) throws Exception {
		User user = userRepository.findByUsername(userName);
		if(user == null){
			return null;
		}
		userRepository.delete(user);
		return user;
	}

	@Override
	@Transactional
	public User updateUser(User user) throws Exception {
		
		if(user == null && userRepository.findByUsername(user.getUsername()) == null){
			throw new Exception();
		}
		
		user = userRepository.saveAndFlush(user);
		return user;
	}

	@Override
	@Transactional
	public User getUser(String userName) throws Exception {
		return userRepository.findByUsername(userName);
	}
}
