package com.Assignment.group.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	
	
	public Optional<User> findByUserName(String userName) {
 
		return userRepository.findByUserName(userName);
	}

 
}
