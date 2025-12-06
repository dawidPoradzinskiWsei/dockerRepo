package com.example.restservice;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private UserRepository userRepository;

	GreetingController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/greeting")
	public ResponseEntity<String> greeting(@RequestParam(defaultValue = "World") String name) {
		Optional<User> optionalUser = userRepository.findByName(name);

		return optionalUser.isPresent() ? new ResponseEntity<String>("Hello " + name, HttpStatus.OK) : new ResponseEntity<String>("Not found " + name, HttpStatus.NOT_FOUND);	
	}

	@PostMapping("/greeting")
	public ResponseEntity<User> addUser(@RequestBody AddUserBody addUserBody) {
		Optional<User> optionalUser = userRepository.findByName(addUserBody.name);

		if(optionalUser.isPresent()) {
			return new ResponseEntity<User>(HttpStatus.FOUND);
		} else {
			User newUser = new User();
			newUser.setName(addUserBody.name);
			userRepository.save(newUser);
			return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
		}
	}

	@DeleteMapping("/greeting")
	public ResponseEntity<String> deleteUser(@RequestBody AddUserBody addUserBody) {
		Optional<User> optionalUser = userRepository.findByName(addUserBody.name);

		if(optionalUser.isPresent()) {
			userRepository.delete(optionalUser.get());
			return new ResponseEntity<String>("User deleted: " + addUserBody.name, HttpStatus.GONE);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}