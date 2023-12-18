package com.Assignment.group.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Assignment.group.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUserName(String username);

}
