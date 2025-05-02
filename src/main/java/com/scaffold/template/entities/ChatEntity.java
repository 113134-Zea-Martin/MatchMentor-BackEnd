package com.scaffold.template.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Representa la entidad de un mensaje en el sistema.
 * Cada mensaje está asociado a un match y contiene información
 * sobre el remitente, contenido, fecha de envío y estado de lectura.
 */
@Data
@Entity
@Table(name = "chats") // Define la tabla en la base de datos asociada a esta entidad
public class ChatEntity {

    /**
     * Identificador único del mensaje.
     * Se genera automáticamente utilizando la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador del match al que pertenece el mensaje.
     */
    private Long matchId;

    /**
     * Identificador del usuario que envió el mensaje.
     */
    private Long senderId;

    /**
     * Contenido del mensaje.
     */
    private String content;

    /**
     * Fecha y hora en que se envió el mensaje.
     */
    private LocalDateTime timestamp;

    /**
     * Indica si el mensaje ha sido leído o no.
     * Por defecto, se inicializa como falso.
     */
    private Boolean isRead = false;
}