package com.scaffold.template.services.chat;

import com.scaffold.template.dtos.chat.ChatMessage;
import com.scaffold.template.entities.ChatEntity;
import com.scaffold.template.repositories.ChatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public ChatEntity saveChat(ChatMessage chatMessage) {
        // Crear una nueva entidad de chat a partir del mensaje recibido
        ChatEntity chat = new ChatEntity();
        chat.setMatchId(chatMessage.getMatchId());
        chat.setSenderId(chatMessage.getSenderId());
        chat.setContent(chatMessage.getContent());
        chat.setTimestamp(LocalDateTime.now());
        chat.setIsRead(false); // Inicialmente, el mensaje no está leído
        // Guardar el chat en la base de datos
        return chatRepository.save(chat);
    }

    @Override
    public List<ChatMessage> getChatHistory(Long matchId) {
        // Obtener el historial de chats para un match específico
        List<ChatEntity> chatEntities = chatRepository.findByMatchId(matchId);
        // Ordenar los mensajes por fecha de creación (primero el más antiguo)
        chatEntities.sort((c1, c2) -> c1.getTimestamp().compareTo(c2.getTimestamp()));
        // Convertir las entidades de chat a mensajes de chat
        return chatEntities.stream()
                .map(chat -> {
                    ChatMessage message = new ChatMessage();
                    message.setMatchId(chat.getMatchId());
                    message.setSenderId(chat.getSenderId());
                    message.setContent(chat.getContent());
                    message.setTimestamp(chat.getTimestamp());
                    return message;
                })
                .toList();
    }

    @Override
    public void markMessageAsRead(Long matchId, Long userId) {
        // Marcar un mensaje como leído para un usuario específico
        List<ChatEntity> chatEntities = chatRepository.findByMatchIdAndSenderIdAndIsReadFalse(matchId, userId);
        for (ChatEntity chat : chatEntities) {
            chat.setIsRead(true); // Marcar el mensaje como leído
            chatRepository.save(chat); // Guardar los cambios en la base de datos
        }
    }

    @Override
    public LocalDateTime getLastMessageDate(Long matchId) {
        List<ChatEntity> chatEntities = chatRepository.findByMatchId(matchId);
        if (chatEntities.isEmpty()) {
            return null;
        }
        // Encontrar el mensaje con la fecha más reciente
        return chatEntities.stream()
                .map(ChatEntity::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public Boolean existsByMatchIdAndSenderIdAndIsReadFalse(Long matchId, Long senderId) {
        // Verificar si existe un mensaje no leído para un match específico y un remitente específico
        return chatRepository.existsByMatchIdAndSenderIdAndIsReadFalse(matchId, senderId);
    }

}
