package com.bhavani.controller;

import com.bhavani.config.JwtProvider;
import com.bhavani.model.User;
import com.bhavani.repository.UserRepository;
import com.bhavani.request.LoginRequest;
import com.bhavani.response.AuthResponse;
import com.bhavani.service.CustomUserDetailsImpl;

import com.bhavani.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsImpl customUserDetails;

    @Autowired
    private SubscriptionService subscriptionService;

    // ========== SIGNUP ==========
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        // Check if user already exists
        if (userRepository.findAllByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists with another account.");
        }

        // Save new user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        subscriptionService.createSubscription(savedUser);

        // Authenticate newly created user
        UserDetails userDetails = customUserDetails.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse(jwt, "Signup successful");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ========== SIGNIN ==========
    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Authenticate user
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse(jwt, "Signin successful");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ========== AUTHENTICATION ==========
    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserDetails.loadUserByUsername(email);

        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
