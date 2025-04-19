package com.scaffold.template.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
     * Fecha de nacimiento del usuario.
     * Campo opcional.
     */
    private LocalDate birthDate;

    /**
     * Ubicación del usuario.
     * Campo opcional.
     */
    private String location;

    // Campos específicos para estudiantes

    /**
     * Nivel educativo del estudiante.
     * Campo opcional.
     */
    private String educationLevel;

    /**
     * Área de estudio del estudiante.
     * Campo opcional.
     */
    private String studyArea;

    /**
     * Institución educativa del estudiante.
     * Campo opcional.
     */
    private String institution;

    /**
     * Año de graduación del estudiante.
     * Campo opcional.
     */
    private String graduationYear;

    /**
     * Objetivos de mentoría del estudiante.
     * Campo opcional.
     */
    private String mentoringGoals;

    // Campos específicos para tutores

    /**
     * Profesión actual del tutor.
     * Campo opcional.
     */
    private String currentProfession;

    /**
     * Empresa u organización del tutor.
     * Campo opcional.
     */
    private String company;

    /**
     * Años de experiencia del tutor en su campo.
     * Campo opcional.
     */
    private int yearsOfExperience;

    /**
     * Biografía profesional del tutor.
     * Campo opcional.
     */
    private String professionalBio;

    /**
     * Tarifa por hora del tutor.
     * Campo opcional.
     */
    private double hourlyRate;

    // Campos comunes para estudiantes y tutores

    /**
     * Lista de intereses asociados al usuario.
     * Relación uno a muchos con la entidad UserInterestEntity.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInterestEntity> userInterests = new ArrayList<>();

    /**
     * Rol del usuario en el sistema.
     * Almacenado como una cadena en la base de datos.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * URL del perfil de LinkedIn del usuario.
     * Campo opcional.
     */
    private String linkedinUrl;

    /**
     * Biografía del usuario.
     * Campo opcional.
     */
    private String bio;

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