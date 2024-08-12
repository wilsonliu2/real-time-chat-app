package com.wilson.chat.app.demo.auth;

import com.wilson.chat.app.demo.config.JwtService;
import com.wilson.chat.app.demo.entity.User.User;
import com.wilson.chat.app.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Method to register new user
    public AuthResponse register(User request) {
        // Create a new User object and set its properties from request
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user); // Save user to repository

        // Generate a JWT token for the new user
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        // Return AuthResponse containing the token
        return new AuthResponse(token);
    }

    // Method for authenticating existing user
    public AuthResponse login(AuthRequest authenticationRequest){
        // Create an auth token given provided user and password
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        );

        // Authentticate user using AuthenticationManager
        authenticationManager.authenticate(authToken);

        // Retrieve the authenticated user from the user repository
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).get();

        // Generate jwt token
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        // Return an auth response with the jwt token
        return new AuthResponse(jwt);
    }

    // Private method for generating extra claims for the JWT token
    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("username", user.getUsername());
        extraClaims.put("role", user.getRole().name());
        return extraClaims;
    }
}