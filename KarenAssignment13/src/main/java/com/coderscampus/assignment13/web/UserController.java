package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {

		model.put("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		List<User> existingUser = userService.findByUsername(user.getUsername());
		if (!existingUser.isEmpty()) {
			// User already exists, handle the situation (display error message, redirect,
			// etc.)
			// For example, you can set an error message in the model and redirect back to
			// the registration page:
			return "redirect:/register?error=User already exists";
		} else {
			userService.saveUser(user);
			return "redirect:/register";
		}
	}
//	public String postCreateUser (User user) {
//		System.out.println(user);
//		userService.saveUser(user);
//		return "redirect:/register";
//	}

//	@GetMapping("/users")
//	public String getAllUsers(ModelMap model) {
//	    Set<User> users = userService.findAll();
//
//	    // Remove duplicate users by creating a new set with distinct users
//	    Set<User> distinctUsers = new HashSet<>(users);
//
//	    model.put("users", distinctUsers);
//	    if (distinctUsers.size() == 1) {
//	        model.put("user", distinctUsers.iterator().next());
//	    }
//
//	    return "users";
//	}
	@GetMapping("/users")
	public String getAllUsers(ModelMap model) {
		Set<User> users = userService.findAll();

		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}

		return "users";
	}
	

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		return "users";
	}

	@PutMapping("/users/{userId}")
	public String putOneUser(@PathVariable Long userId, @ModelAttribute User user) {
	    user.setUserId(userId);
	    User savedUser = userService.saveUser(user);
	    return "redirect:/users/" + savedUser.getUserId();
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(@PathVariable Long userId, @ModelAttribute User user) {
	    user.setUserId(userId);
	    userService.saveUser(user);
	    return "redirect:/users/" + userId;
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
	@PostMapping("/users/{userId}/accounts")
	public String createAccount(@PathVariable Long userId) {
	    userService.createAccount(userId);
	    
	    return "redirect:/account.html";
	}
}
