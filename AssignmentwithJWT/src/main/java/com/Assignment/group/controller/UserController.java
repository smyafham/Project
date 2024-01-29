package com.Assignment.group.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.group.common.UserConstant;
import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());


	@Autowired
	    private UserRepository userRepository;
	 	
	 	@Autowired
	 	private BCryptPasswordEncoder passwordEncoder;
	 	//pass
	 	@GetMapping("/all")
	    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority('ADMIN') ")
	    public List<User> getAllUsers() {
	 		LOGGER.log(Level.INFO, "Fetching all users");
	 		System.out.println("in url getall");
	        List<User> users = userRepository.findAll();
	        return  users;
	    }

	 	//pass
	 	@GetMapping("/{id}")
	 	@PreAuthorize("hasAuthority('SUPERADMIN')")
	 	@Secured("SUPERADMIN")
	    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
	 		LOGGER.log(Level.INFO, "Fetching user by ID: {0}", id);
	        Optional<User> user = userRepository.findById(id);
	        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	    }

	 	//pass
	 	@CrossOrigin("http:localhost:4200")
	    @PostMapping("/create")
	    public String createUser(@RequestBody User user) throws Exception {
	        LOGGER.log(Level.INFO, "Attempting to create user: {0}", user.getUserName());

	    	List<User> ls = userRepository.findAll();
	    	List<String> usernameList = ls.stream()
	    	        .map(User::getUserName) // Assuming getUsername() method exists in User class
	    	        .collect(Collectors.toList());
	    	

	    	if(!usernameList.contains(user.getUserName())&& user.getUserName()!=null) {
	    		
	    	user.setRoles(UserConstant.DEFAULT_ROLE);//USER
	    	String encryptedpassword= passwordEncoder.encode(user.getPassword());
	    	user.setPassword(encryptedpassword);
	    	user.isActive(true);
	        userRepository.save(user);
            LOGGER.log(Level.INFO, "User {0} has been created", user.getUserName());
	        return user.getUserName()+" has been created";
	    }else {
            LOGGER.log(Level.WARNING, "User already exists");
	        throw new Exception("User already exists"); // Custom exception for user already existing
	    }

	    }

	    //pass
	    @PutMapping("/update/{id}")
	    @PreAuthorize("hasAuthority('SUPERADMIN')")
	 	@Secured("SUPERADMIN")
	    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
	        LOGGER.log(Level.INFO, "Attempting to update user with ID: {0}", id);
	        Optional<User> optionalUser = userRepository.findById(id);
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            user.setUserName(userDetails.getUserName());
	            System.out.println(userDetails.getPassword());
	            String encryptedpassword= passwordEncoder.encode(userDetails.getPassword());
		    	user.setPassword(encryptedpassword);
		    	System.out.println(user.getPassword());
	            user.setRoles(userDetails.getRoles()); // Update other fields as needed
	            user.isActive(userDetails.isActive());
	            User updatedUser = userRepository.save(user);
	            return ResponseEntity.ok(updatedUser);
	        } else {
	            LOGGER.log(Level.WARNING, "User with ID {0} not found for update", id);
	            return ResponseEntity.notFound().build();
	        }
	    }

	    //pass
	    @DeleteMapping("/delete/{id}")
	    @PreAuthorize("hasAuthority('SUPERADMIN')")
	 	@Secured("SUPERADMIN")
	    public String deleteUser(@PathVariable Integer id){
	        LOGGER.log(Level.INFO, "Attempting to delete user with ID: {0}", id);
	    	 try {
	             User user = userRepository.findById(id).orElse(null);
	             
	             if (user != null) {
	                 String name = user.getUserName();
	                 userRepository.deleteById(id);
	                 LOGGER.log(Level.INFO, "User {0} has been deleted", name);
	                 return name + " has been deleted";
	             } else {
	                 LOGGER.log(Level.WARNING, "User with ID {0} does not exist", id);
	                 return "User with ID " + id + " does not exist";
	             }
	         } catch (Exception e) {
	             LOGGER.log(Level.SEVERE, "An error occurred while deleting the user", e);
	             return "An error occurred while deleting the user";
	         }
	     }
	        
	    
	    //pass
//	    @GetMapping("access/{userId}/{userRole}")
//	    @PreAuthorize("hasAuthority('SUPERADMIN')")
//	 	@Secured("SUPERADMIN")
//	    public String giveAccessTOUser(@PathVariable int userId,@PathVariable String userRole, Principal principal) {
//	    	User user=userRepository.findById(userId).get();
//	    	List<String> activeRoles= getRolesByLoggedInUser(principal);
//	    	String newRole="";
//	    	if(activeRoles.contains(userRole)) {
//	    		newRole=user.getRoles()+","+userRole;
//	    		user.setRoles(newRole);
//	    	}
//			userRepository.save(user);
//	    	return "Hi "+user.getUserName()+" new roles has been assigned to you by "+principal.getName();
//			
//		}
//	    
	    
	    private User getLoggedInUSer(Principal principal) {
	    	return userRepository.findByUserName(principal.getName()).get();
		}
	    
	    
	    //check which role user has if superadmin can give superadmin and admin access
	    List<String> getRolesByLoggedInUser(Principal principal) {
	    	String roles=getLoggedInUSer(principal).getRoles();
		
		List<String> assignRoles=Arrays.stream(roles.split(",")).collect(Collectors.toList());
		if(assignRoles.contains("SUPERADMIN")) {
			return Arrays.stream(UserConstant.SUPERADMIN_ACCESS).collect(Collectors.toList());
			
		}
		if(assignRoles.contains("ADMIN")) {
			return Arrays.stream(UserConstant.ADMIN_ACCESS).collect(Collectors.toList());
			
		}
		return Collections.emptyList();
		}
	    
	    @GetMapping("/test")
	 	@PreAuthorize("hasAuthority('USER')")
	    public String testUserAccess() {
	    	return "user can only access this";
			
		}
	    
	   
	    
	    
}
