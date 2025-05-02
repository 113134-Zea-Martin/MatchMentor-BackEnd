package com.scaffold.template.services.chat;

import com.scaffold.template.entities.ChatEntity;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {
    ChatEntity createChat(Long matchId);
    //Método para crear un chat de bienvenida, cuando se crea un match
    void createWelcomeChat(Long matchId);
}
