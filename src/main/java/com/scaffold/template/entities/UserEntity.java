package com.scaffold.template.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad que representa a un usuario en el sistema.
 * Utiliza anotaciones de JPA para mapear la clase a una tabla en la base de datos.
 */
@Entity
@Data
@Table(name = "users")
public class UserEntity {

    /**
     * Identificador único del usuario.
     * Generado automáticamente con la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario.
     */
    private String firstName;

    /**
     * Apellido del usuario.
     */
    private String lastName;

    /**
     * Correo electrónico del usuario.
     * Debe ser único y no puede ser nulo.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Contraseña del usuario.
     * No puede ser nula.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Intereses del usuario.
     * Campo opcional.
     */
    private String interests;

    /**
     * Rol del usuario en el sistema.
     * Almacenado como una cadena en la base de datos.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Fecha y hora de creación del usuario.
     * Representa el momento en que se creó el registro.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Indica si el usuario está activo.
     * Puede ser utilizado para deshabilitar cuentas sin eliminarlas.
     */
    private Boolean isActive;
}