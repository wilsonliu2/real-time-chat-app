package com.wilson.chat.app.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Configure message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple in-memory message broker with a destination prefix "/topic"
        config.enableSimpleBroker("/topic");
        // Set app destination prefix to "/app"
        config.setApplicationDestinationPrefixes("/app");
    }

    // Register STOMP endpoints
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Add endpoint at "/ws"
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Allow connections from any origin for now
                .withSockJS();
    }
}
