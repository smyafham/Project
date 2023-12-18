package com.Assignment.group.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Assignment.group.common.UserConstant;
import com.Assignment.group.dto.UserDto;
import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;

@Service
public class GroupUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository repository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user= repository.findByUserName(username);
		return user.map(GroupUserDetails :: new)
				.orElseThrow(()->new UsernameNotFoundException(username+ "doesnt exist"));
	}
	
	public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());

		// encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.isActive(true);
        user.setRoles(UserConstant.DEFAULT_ROLE);//USER

        repository.save(user);
        
        
    }
 
}
