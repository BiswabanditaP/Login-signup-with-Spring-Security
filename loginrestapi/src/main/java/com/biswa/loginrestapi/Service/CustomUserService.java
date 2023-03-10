package com.biswa.loginrestapi.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.biswa.loginrestapi.Entity.User;

import com.biswa.loginrestapi.Repository.UserRepository;



@Service
public class CustomUserService implements UserDetailsService {

     @Autowired
	 private UserRepository userRepository;
     
     @Autowired
	    private PasswordEncoder passwordEncoder;
  

	    public CustomUserService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }
	    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;

	
	@Override
	public UserDetails loadUserByUsername(String usernameorEmail) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameOrEmail(usernameorEmail, usernameorEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: "+ usernameorEmail));

       Set<GrantedAuthority> authorities = user
               .getRoles()
               .stream()
               .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

       return new org.springframework.security.core.userdetails.User(user.getEmail(),
               user.getPassword(),
               authorities);
		
	}
	public String forgotPassword(String email) {

		Optional<User> userOptional = userRepository.findByEmail(email);

		if (!userOptional.isPresent()) {
			return "Invalid email id.";
		}

		User user = userOptional.get();
		user.setToken(generateToken());
		user.setTokenCreationDate(LocalDateTime.now());

		user = userRepository.save(user);

		return user.getToken();
	}

	public String resetPassword(String token, String password) {

		Optional<User> userOptional = Optional
				.ofNullable(userRepository.findByToken(token));

		if (!userOptional.isPresent()) {
			return "Invalid token.";
		}

		LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			return "Token expired.";

		}

		User user = userOptional.get();

		user.setPassword(passwordEncoder.encode(password));
		user.setToken(null);
		user.setTokenCreationDate(null);

		userRepository.save(user);

		return "Your password successfully updated.";
	}

	
	private String generateToken() {
		StringBuilder token = new StringBuilder();

		return token.append(UUID.randomUUID().toString())
				.append(UUID.randomUUID().toString()).toString();
	}

	
	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}
	


}
