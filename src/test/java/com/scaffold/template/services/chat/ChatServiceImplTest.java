package com.scaffold.template.services.chat;

import com.scaffold.template.dtos.chat.ChatMessage;
import com.scaffold.template.entities.ChatEntity;
import com.scaffold.template.repositories.ChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Nested
    @DisplayName("saveChat")
    class SaveChat {

        @Test
        void savesChatMessageAndReturnsEntity() {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMatchId(1L);
            chatMessage.setSenderId(2L);
            chatMessage.setContent("Hola");

            ChatEntity savedEntity = new ChatEntity();
            savedEntity.setMatchId(1L);
            savedEntity.setSenderId(2L);
            savedEntity.setContent("Hola");
            savedEntity.setTimestamp(LocalDateTime.now());
            savedEntity.setIsRead(false);

            when(chatRepository.save(any(ChatEntity.class))).thenReturn(savedEntity);

            ChatEntity result = chatService.saveChat(chatMessage);

            assertThat(result.getMatchId()).isEqualTo(1L);
            assertThat(result.getSenderId()).isEqualTo(2L);
            assertThat(result.getContent()).isEqualTo("Hola");
            assertThat(result.getIsRead()).isFalse();
            verify(chatRepository).save(any(ChatEntity.class));
        }
    }

    @Nested
    @DisplayName("getChatHistory")
    class GetChatHistory {

        @Test
        void returnsOrderedChatMessagesForMatch() {
            ChatEntity chat1 = new ChatEntity();
            chat1.setMatchId(1L);
            chat1.setSenderId(2L);
            chat1.setContent("Primero");
            chat1.setTimestamp(LocalDateTime.now().minusMinutes(10));

            ChatEntity chat2 = new ChatEntity();
            chat2.setMatchId(1L);
            chat2.setSenderId(3L);
            chat2.setContent("Segundo");
            chat2.setTimestamp(LocalDateTime.now());

            when(chatRepository.findByMatchId(1L)).thenReturn(Arrays.asList(chat2, chat1));

            List<ChatMessage> history = chatService.getChatHistory(1L);

            assertThat(history).hasSize(2);
            assertThat(history.get(0).getContent()).isEqualTo("Primero");
            assertThat(history.get(1).getContent()).isEqualTo("Segundo");
        }

        @Test
        void returnsEmptyListWhenNoMessages() {
            when(chatRepository.findByMatchId(99L)).thenReturn(Collections.emptyList());
            List<ChatMessage> history = chatService.getChatHistory(99L);
            assertThat(history).isEmpty();
        }
    }

    @Nested
    @DisplayName("markMessageAsRead")
    class MarkMessageAsRead {

        @Test
        void marksUnreadMessagesAsRead() {
            ChatEntity chat1 = new ChatEntity();
            chat1.setIsRead(false);

            ChatEntity chat2 = new ChatEntity();
            chat2.setIsRead(false);

            when(chatRepository.findByMatchIdAndSenderIdAndIsReadFalse(1L, 2L))
                    .thenReturn(Arrays.asList(chat1, chat2));

            chatService.markMessageAsRead(1L, 2L);

            assertThat(chat1.getIsRead()).isTrue();
            assertThat(chat2.getIsRead()).isTrue();
            verify(chatRepository, times(2)).save(any(ChatEntity.class));
        }

        @Test
        void doesNothingIfNoUnreadMessages() {
            when(chatRepository.findByMatchIdAndSenderIdAndIsReadFalse(1L, 2L))
                    .thenReturn(Collections.emptyList());

            chatService.markMessageAsRead(1L, 2L);

            verify(chatRepository, never()).save(any(ChatEntity.class));
        }
    }

    @Nested
    @DisplayName("getLastMessageDate")
    class GetLastMessageDate {

        @Test
        void returnsNullIfNoMessages() {
            when(chatRepository.findByMatchId(1L)).thenReturn(Collections.emptyList());
            assertThat(chatService.getLastMessageDate(1L)).isNull();
        }

        @Test
        void returnsMostRecentTimestamp() {
            LocalDateTime t1 = LocalDateTime.now().minusHours(1);
            LocalDateTime t2 = LocalDateTime.now();
            ChatEntity chat1 = new ChatEntity();
            chat1.setTimestamp(t1);
            ChatEntity chat2 = new ChatEntity();
            chat2.setTimestamp(t2);

            when(chatRepository.findByMatchId(1L)).thenReturn(Arrays.asList(chat1, chat2));

            assertThat(chatService.getLastMessageDate(1L)).isEqualTo(t2);
        }
    }

    @Nested
    @DisplayName("existsByMatchIdAndSenderIdAndIsReadFalse")
    class ExistsUnreadMessage {

        @Test
        void returnsTrueIfUnreadMessageExists() {
            when(chatRepository.existsByMatchIdAndSenderIdAndIsReadFalse(1L, 2L)).thenReturn(true);
            assertThat(chatService.existsByMatchIdAndSenderIdAndIsReadFalse(1L, 2L)).isTrue();
        }

        @Test
        void returnsFalseIfNoUnreadMessageExists() {
            when(chatRepository.existsByMatchIdAndSenderIdAndIsReadFalse(1L, 2L)).thenReturn(false);
            assertThat(chatService.existsByMatchIdAndSenderIdAndIsReadFalse(1L, 2L)).isFalse();
        }
    }

    @Nested
    @DisplayName("createWelcomeChat")
    class CreateWelcomeChat {

        @Test
        void savesWelcomeMessageWithNullSender() {
            ArgumentCaptor<ChatEntity> captor = ArgumentCaptor.forClass(ChatEntity.class);

            chatService.createWelcomeChat(123L);

            verify(chatRepository).save(captor.capture());
            ChatEntity saved = captor.getValue();
            assertThat(saved.getMatchId()).isEqualTo(123L);
            assertThat(saved.getSenderId()).isNull();
            assertThat(saved.getContent()).contains("Advertencia");
        }
    }
}