package com.scaffold.template.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID de la notificación

    private Long userId; // ID del usuario al que se le envía la notificación

    private NotificationType notificationType; // Tipo de notificación (ej. MATCH, VIEWED_PROFILE, etc.)

    private String message; // Mensaje de la notificación

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // Fecha y hora de creación de la notificación

    private Boolean isRead; // Indica si la notificación ha sido leída o no

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readedAt; // Fecha y hora de lectura de la notificación

    private Long relatedEntityId; // ID de la entidad relacionada (ej. ID del match o perfil visto
}
