package com.scaffold.template.dtos.match;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa la información de un match confirmado.
 * Este objeto se utiliza para transferir datos relacionados con matches confirmados
 * entre las capas de la aplicación.
 */
@Data
public class ConfirmedMatchResponseDTO {

    /**
     * Identificador único del match confirmado.
     */
    private Long id;

    /**
     * Identificador del usuario asociado al match confirmado.
     */
    private Long userId;

    /**
     * Nombre completo del usuario asociado al match confirmado.
     */
    private String fullName;

    /**
     * Rol del usuario en el sistema.
     * Este campo indica si el usuario es un estudiante o un tutor.
     */
    private Role role;

    /**
     * Descripción adicional del match confirmado.
     * Este campo puede contener información relevante sobre el match.
     */
    private String description;

    /**
     * Fecha y hora de la última actualización del match confirmado.
     * Formato: yyyy-MM-dd'T'HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * Indica si el match confirmado está activo.
     * Este campo puede ser utilizado para determinar si el match sigue vigente.
     */
    private Boolean isActive;

    private LocalDateTime lastMessageDate;

    private Boolean hasUnreadMessages;

    private List<String> commonInterests;
}