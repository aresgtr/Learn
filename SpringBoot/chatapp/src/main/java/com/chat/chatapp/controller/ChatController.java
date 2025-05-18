package com.chat.chatapp.controller;

import com.chat.chatapp.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    // /app/sendMessage
    @MessageMapping("/sendMessage") // whenever a client sends a message to this endpoint, this method is called
    @SendTo("/topic/messages")  // send to all clients subscribe to this topic
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @GetMapping("chat") // whoever visit the chat URL goes to chat.html
    public String chat() {
        return "chat";
    }
}
