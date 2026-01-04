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

    @Autowired
    private JwtProvider jwtProvider; // ✅ inject provider

    // ========== SIGNUP ==========
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {

        if (userRepository.findAllByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists with another account."
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        subscriptionService.createSubscription(savedUser);

        UserDetails userDetails =
                customUserDetails.loadUserByUsername(user.getEmail());

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ FIX: instance method call
        String jwt = jwtProvider.generateToken(authentication);

        return new ResponseEntity<>(
                new AuthResponse(jwt, "Signup successful"),
                HttpStatus.CREATED
        );
    }

    // ========== SIGNIN ==========
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        Authentication authentication =
                authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ✅ FIX: instance method call
        String jwt = jwtProvider.generateToken(authentication);

        return ResponseEntity.ok(
                new AuthResponse(jwt, "Signin successful")
        );
    }

    // ========== AUTH ==========
    private Authentication authenticate(String email, String password) {

        UserDetails userDetails =
                customUserDetails.loadUserByUsername(email);

        if (userDetails == null ||
                !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}
