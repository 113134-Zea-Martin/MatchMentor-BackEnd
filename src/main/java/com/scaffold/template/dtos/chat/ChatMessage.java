package com.scaffold.template.dtos.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Long matchId;
    private Long senderId;
    private String content;
    private LocalDateTime timestamp;
}
