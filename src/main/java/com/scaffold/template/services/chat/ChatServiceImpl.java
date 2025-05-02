package com.scaffold.template.services.chat;

import com.scaffold.template.entities.ChatEntity;
import com.scaffold.template.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatServiceImpl implements ChatService {

    // Mensaje de bienvenida al crear un chat
    private static final String WELCOME_MESSAGE = "Advertencia: Por favor, no compartan información personal sensible " +
            "(como datos bancarios, documentos de identidad, contraseñas, etc.) a través de este chat.";

    // Inyección de dependencias del repositorio de chats
    private final ChatRepository chatRepository;

    // Constructor para inicializar el repositorio
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public ChatEntity createChat(Long matchId) {
        return null;
    }

    @Override
    public void createWelcomeChat(Long matchId) {
        // Crear un nuevo chat con el mensaje de bienvenida
        ChatEntity chat = new ChatEntity();
        chat.setMatchId(matchId);
        chat.setSenderId(null); // El remitente es nulo al crear el chat
        chat.setContent(WELCOME_MESSAGE);
        chat.setTimestamp(LocalDateTime.now());
        // Guardar el chat en la base de datos
        chatRepository.save(chat);
    }

}
