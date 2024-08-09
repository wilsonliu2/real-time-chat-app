package com.wilson.chat.app.demo.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Mark class as Rest Controller, base URL for this controller is /auth
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Handles POST requests to /register
    // Accepts a User object from the request body and returns an auth response
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User request) {
        // Call the register method of authService
        return ResponseEntity.ok(authService.register(request));
    }

    // Handle POST requests to /authenticate
    // Accepts a User object from the request body and returns an auth response
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        // Call the login method of authService
        return ResponseEntity.ok(authService.login(authRequest));
    }
}