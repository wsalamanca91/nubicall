package com.nubicall.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nubicall.model.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	public User findByUsername(String userName);
	
}
