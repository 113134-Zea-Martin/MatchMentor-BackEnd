package com.scaffold.template.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Representa la entidad que modela los intereses asociados a un usuario.
 * Cada instancia de esta clase representa una relación entre un usuario y un interés específico.
 */
@Entity
@Data
@Table(name = "user_interests")
public class UserInterestEntity {

    /**
     * Identificador único del interés del usuario.
     * Generado automáticamente con la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuario asociado al interés.
     * Relación de muchos a uno con la entidad UserEntity.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * Interés asociado al usuario.
     * Relación de muchos a uno con la entidad InterestEntity.
     */
    @ManyToOne
    @JoinColumn(name = "interest_id", nullable = false)
    private InterestEntity interest;

    /**
     * Fecha y hora en que se creó la relación entre el usuario y el interés.
     * Se utiliza para registrar cuándo se estableció esta relación.
     */
    private LocalDateTime createdAt;

    /**
     * Indica si la relación entre el usuario y el interés está activa.
     * true si está activa, false en caso contrario.
     */
    private boolean isActive;

}