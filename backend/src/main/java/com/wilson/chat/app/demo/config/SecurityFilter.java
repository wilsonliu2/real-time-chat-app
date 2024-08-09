package com.wilson.chat.app.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilter {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityFilter(AuthenticationProvider authenticationProvider, JwtAuthFilter jwtAuthFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF protection as JWT is being used
                .csrf(csrfConfig -> csrfConfig.disable())
                // Set session management to stateless as JWT is being used
                .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Set the custom authentication provider
                .authenticationProvider(authenticationProvider)
                // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configure authorization rules
                .authorizeHttpRequests( authConfig -> {
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();

                    authConfig.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.USER.name());
                    authConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.ADMIN.name());
                    authConfig.requestMatchers(HttpMethod.GET, "/user-details").authenticated();
                    authConfig.requestMatchers(HttpMethod.POST, "/user-details").authenticated();

                    authConfig.anyRequest().denyAll();


                });

        return http.build();

    }
}