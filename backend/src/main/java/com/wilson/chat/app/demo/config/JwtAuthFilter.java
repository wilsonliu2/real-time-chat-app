package com.wilson.chat.app.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Override doFilterInternal to provide custom JWT authentication logic
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtain authorization header that contain the JWT token
        String authHeader = request.getHeader("Authorization");

        // If authorization header is missing, or it does not start with Bearer continue to the next filter
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from the header
        String jwt = authHeader.split(" ")[1];

        // Check if the JWT token is valid and not expired
        if (!jwtService.isTokenValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract username from the JWT token
        String username = jwtService.extractUsername(jwt);

        // Get user and set authentication object inside the security context
        User user = userRepository.findByUsername(username).get();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );

        // Set the authentication object in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Execute the rest of the filters
        filterChain.doFilter(request, response);
    }
}