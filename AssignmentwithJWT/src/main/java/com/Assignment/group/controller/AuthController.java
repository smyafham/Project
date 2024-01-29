package com.Assignment.group.controller;



import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Assignment.group.common.UserConstant;
import com.Assignment.group.config.JwtUtils;
import com.Assignment.group.dto.UserDto;
import com.Assignment.group.entity.JwtRequest;
import com.Assignment.group.entity.JwtResponse;
import com.Assignment.group.entity.User;
import com.Assignment.group.repository.UserRepository;
import com.Assignment.group.service.GroupUserDetailsService;
import com.Assignment.group.service.UserService;

import net.bytebuddy.implementation.bytecode.Throw;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	
	@Autowired
	GroupUserDetailsService groupUserDetailsservice;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	 BCryptPasswordEncoder passwordEncoder;
	
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

	  // handler method to handle user registration form submit request
	    @PostMapping("/register/save")
	    public String registration(@Validated @ModelAttribute("user") UserDto userDto,
	                               BindingResult result,
	                               Model model) throws Exception{
	        LOGGER.log(Level.INFO, "Executing registration method");
	    	System.out.println("in register save");
	    	  userDto.setActive(true);
	          userDto.setRoles(UserConstant.DEFAULT_ROLE);//USER
	 
	    	List<User> ls = userRepository.findAll();
	    	List<String> usernameList = ls.stream()
	    	        .map(User::getUserName) // Assuming getUsername() method exists in User class
	    	        .collect(Collectors.toList());
	    	

	    	if(!usernameList.contains(userDto.getUserName())&& userDto.getUserName()!=null) {
	    		 groupUserDetailsservice.saveUser(userDto);
	             LOGGER.log(Level.INFO, "Registration successful for user: " + userDto.getUserName());
	 	        return "redirect:/register?success";
	 	        }
	        LOGGER.log(Level.WARNING, "User with the same email already exists");
	        throw new Exception("User with the same email already exists");

	    	}
	    	
//	       
//	    
	    
	    // handler method to handle login request
//	    @GetMapping("/login")
//	    public String login(){
//	        return "login";
//	    }
//	    @GetMapping("/page1")
//	    @PreAuthorize("hasAuthority('USER')")
//	    public String page1(){
//	        return "page1";
//	    }
//	    
//	 // method to get list of users
//	    @GetMapping("/users")
//	    @PreAuthorize("hasAuthority('SUPERADMIN') or hasAuthority('ADMIN') ")
//	    public String users(Model model){
//	        List<User> users = userRepository.findAll();
//	        model.addAttribute("users", users);
//	        return "users";
//	    }
//	    
	    @Autowired
	    private AuthenticationManager authenticationManager;
	    
	    @Autowired
	    private JwtUtils jwtUtils;
	    
	    
	    
	    //generate token
	    @PostMapping("/generate-token")
	    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
	        LOGGER.log(Level.INFO, "Generating token");
	    	try {
	    		System.out.println("in generatetoken");
	    		authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
	    		
	    		
			} catch (UsernameNotFoundException e) {
	            LOGGER.log(Level.SEVERE, "User not found: " + jwtRequest.getUsername(), e);
				throw new Exception("User not found ");
			}
	    	/////authenticate
	    	
	   UserDetails userDetails= 	this.groupUserDetailsservice.loadUserByUsername(jwtRequest.getUsername());
	    String token=	this.jwtUtils.generateToken(userDetails);
        LOGGER.log(Level.INFO, "Token generated successfully for user: " + jwtRequest.getUsername());
	    System.out.println(token);
	    	return ResponseEntity.ok(new JwtResponse(token));
	    }
	    
	    
	    
	    
	    
	    
	
	    void authenticate(String userName,String password)throws Exception {
	    	
	    	try {
	            LOGGER.log(Level.INFO, "Authenticating user: " + userName);
	    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
	            LOGGER.log(Level.INFO, "Authentication successful for user: " + userName);

			} catch (DisabledException e) {
	            LOGGER.log(Level.SEVERE, "User disabled: " + userName, e);
				throw new Exception("USER DISABLED "+e.getMessage());
			}catch (BadCredentialsException e) {
	            LOGGER.log(Level.SEVERE, "Invalid credentials for user: " + userName, e);
				throw new Exception("INVALID CREDENTIALS "+e.getMessage() );
			}
	    	
	    }
	    

		 @GetMapping("/current-user")
		 public User getCurrentUser(Principal principal) throws UsernameNotFoundException {
		        LOGGER.log(Level.INFO, "Getting current user details for: " + principal.getName());
		     Optional<User> userOptional = userRepository.findByUserName(principal.getName());

		     return userOptional.orElseThrow(() ->
		         new UsernameNotFoundException("No user found for " + principal.getName() + ".")
		     );
		 }
		 
		  public class UsernameNotFoundException extends RuntimeException {
		        public UsernameNotFoundException(String message) {
		            super(message);
		        }
		    }
	    
}
