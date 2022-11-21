package com.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.entity.User;
import com.payment.exception.UserNotFoundException;
import com.payment.repository.UserRepository;

@RestController
@RequestMapping("api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public List<User> getUsers() {
		return (List<User>) userRepository.findAll();
	}
	
	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Integer userId) {
		return userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(String.format("User ID [%s] not found.", userId),HttpStatus.NOT_FOUND));
	}
	
	@PostMapping
	public User createUser(@RequestBody User user) {
		return userRepository.save(user);
	}

}
