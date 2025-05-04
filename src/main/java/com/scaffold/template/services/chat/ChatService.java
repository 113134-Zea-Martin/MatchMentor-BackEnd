package com.scaffold.template.services.chat;

import com.scaffold.template.dtos.chat.ChatMessage;
import com.scaffold.template.entities.ChatEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ChatService {
    ChatEntity createChat(Long matchId);
    //Método para crear un chat de bienvenida, cuando se crea un match
    void createWelcomeChat(Long matchId);
    ChatEntity saveChat(ChatMessage chatMessage);

    List<ChatMessage> getChatHistory(Long matchId);

    void markMessageAsRead(Long matchId, Long userId);
    LocalDateTime getLastMessageDate(Long matchId);
    Boolean existsByMatchIdAndSenderIdAndIsReadFalse(Long matchId, Long senderId);
}
