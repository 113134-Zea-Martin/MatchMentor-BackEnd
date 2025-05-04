package com.scaffold.template.controllers.chat;

import com.scaffold.template.dtos.chat.ChatMessage;
import com.scaffold.template.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/{matchId}")
    public List<ChatMessage> getHistory(@PathVariable Long matchId) {
        return chatService.getChatHistory(matchId);
    }

    // Método para marcar un mensaje como leído
    @PutMapping("/{matchId}/read/{userId}")
    public void markMessageAsRead(@PathVariable Long matchId, @PathVariable Long userId) {
        chatService.markMessageAsRead(matchId, userId);
    }
}