package com.wilson.chat.app.demo.controller;

import com.wilson.chat.app.demo.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class ChatController {
    // Handles messages sent to "/app/chat"
    @MessageMapping("/chat")
    // Send returned message to "/topic/messages"
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // Set timestamp on the chat message
        chatMessage.setTimestamp(new Date());
        // Return chat message to "/topic/messages" destination
        return chatMessage;
    }
}
