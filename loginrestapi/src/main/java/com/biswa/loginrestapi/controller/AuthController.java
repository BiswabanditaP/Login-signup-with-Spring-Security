package com.biswa.loginrestapi.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.biswa.loginrestapi.Dto.LoginDto;
import com.biswa.loginrestapi.Dto.SignUpDto;
import com.biswa.loginrestapi.Dto.StatusMessages;
import com.biswa.loginrestapi.Entity.Role;
import com.biswa.loginrestapi.Entity.User;
import com.biswa.loginrestapi.Repository.RoleRepository;
import com.biswa.loginrestapi.Repository.UserRepository;

import com.biswa.loginrestapi.Service.CustomUserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth**")
public class AuthController {
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	 private UserRepository userRepository;

	    @Autowired
	    private RoleRepository roleRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private CustomUserService userService;
	    

	
	
	
 @RequestMapping(value="/signin", 
         method=RequestMethod.POST, 
         produces=MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginDto loginDto){
	 
	 List<User> user =userRepository.findByUsername(loginDto.getUser_name());
	 
	 UsernamePasswordAuthenticationToken authReq
	 = new UsernamePasswordAuthenticationToken( loginDto.getUser_name(), loginDto.getPassword());
	 
	 
	 if(!userRepository.existsByUsername(loginDto.getUser_name())){
		 
		 String error="user not found";
		 return buildResponseEntity(new StatusMessages(HttpStatus.BAD_REQUEST, error));
     }
	 
	String message="login sucessful";

	Authentication auth = authenticationManager.authenticate(authReq);
	
	SecurityContext sc = SecurityContextHolder.getContext();
	sc.setAuthentication(auth);
	 
//	ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
	
	/*
	 * Authentication authentication = authenticationManager.authenticate(new
	 * UsernamePasswordAuthenticationToken( loginDto.getUsernameOrEmail(),
	 * loginDto.getPassword()));
	 * System.out.println(authentication.isAuthenticated());
	 * 
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authentication);
	 */
        
	 return buildResponseEntity(new StatusMessages(HttpStatus.OK,user,message));
    }
 
 @PostMapping("/register")
 public ResponseEntity<Object> registerUser(@RequestBody SignUpDto signUpDto){

     // add check for username exists in a DB
     if(userRepository.existsByUsername(signUpDto.getUsername())){
    	 String error="user already exist";
		 return buildResponseEntity(new StatusMessages(HttpStatus.BAD_REQUEST, error));
     }

     // add check for email exists in DB
     if(userRepository.existsByEmail(signUpDto.getEmail())){
    	 String error="Email is already taken";
    	 return buildResponseEntity(new StatusMessages(HttpStatus.BAD_REQUEST, error));
     }
     User user = new User();
     user.setName(signUpDto.getName());
     user.setUsername(signUpDto.getUsername());
     user.setEmail(signUpDto.getEmail());
     user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

     Role roles = roleRepository.findByName("ROLE_ADMIN").get();
     user.setRoles(Collections.singleton(roles));

     userRepository.save(user);
     String message="Registration Successful";
     List<User>us=userRepository.findByUsername(signUpDto.getUsername());
     return buildResponseEntity(new StatusMessages(HttpStatus.OK,us,message));

 }
 @RequestMapping(value="/forgotpassword",method=RequestMethod.POST)
	public String forgotPassword(@RequestParam(value="email", required = true) String email) {

		String response = userService.forgotPassword(email);

		if (!response.startsWith("Invalid")) {
			response = "http://localhost:8080/api/auth/resetpassword?token=" + response;
		}
		return response;
	}

	@RequestMapping(value="/resetpassword",method=RequestMethod.PUT)
	public ResponseEntity<Object>resetPassword(@RequestParam("token") String token,
			@RequestParam(value="password", required = true) String password) {

		String Message= userService.resetPassword(token, password);
		return buildResponseEntity(new StatusMessages(HttpStatus.OK,Message));

	}

 
 
 private ResponseEntity<Object> buildResponseEntity(StatusMessages message) {
     return new ResponseEntity<>(message, message.getStatus());
 }
 

}
