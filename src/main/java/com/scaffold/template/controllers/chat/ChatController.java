package com.scaffold.template.controllers.chat;

import com.scaffold.template.dtos.chat.ChatMessage;
import com.scaffold.template.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    // Cliente envía a /app/chat.123
    @MessageMapping("/chat.{matchId}")
    @SendTo("/topic/chat.{matchId}")
    public ChatMessage sendToMatch(@DestinationVariable Long matchId,ChatMessage message) {

        message.setTimestamp(LocalDateTime.now());
        message.setMatchId(matchId); // redundante, pero por claridad
        chatService.saveChat(message); // guardar en la base de datos
        return message;
    }
}
