package com.scaffold.template.dtos.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa la información de un usuario en el sistema.
 * Este objeto se utiliza para transferir datos entre las capas de la aplicación.
 */
@Data
public class UserResponseDTO {

    /**
     * Identificador único del usuario.
     */
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
     */
    private String email;

    /**
     * Fecha de nacimiento del usuario.
     * Formato: yyyy-MM-dd.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * Ubicación del usuario.
     */
    private String location;

    // Campos específicos para estudiantes

    /**
     * Nivel educativo del estudiante.
     * Este campo es opcional.
     */
    private String educationLevel;

    /**
     * Área de estudio del estudiante.
     * Este campo es opcional.
     */
    private String studyArea;

    /**
     * Institución educativa del estudiante.
     * Este campo es opcional.
     */
    private String institution;

    /**
     * Año de graduación del estudiante.
     * Este campo es opcional.
     */
    private String graduationYear;

    /**
     * Objetivos de mentoría del estudiante.
     * Este campo es opcional.
     */
    private String mentoringGoals;

    // Campos específicos para tutores

    /**
     * Profesión actual del tutor.
     * Este campo es opcional.
     */
    private String currentProfession;

    /**
     * Empresa u organización donde trabaja el tutor.
     * Este campo es opcional.
     */
    private String company;

    /**
     * Años de experiencia del tutor en su campo profesional.
     * Este campo es opcional.
     */
    private int yearsOfExperience;

    /**
     * Biografía profesional del tutor.
     * Este campo es opcional.
     */
    private String professionalBio;

    /**
     * Tarifa por hora del tutor.
     * Este campo es opcional.
     */
    private double hourlyRate;

    /**
     * Indica si el perfil del tutor es visible para otros usuarios.
     */
    private Boolean isVisible;

    // Campos comunes para estudiantes y tutores

    /**
     * Lista de intereses del usuario.
     */
    private List<String> interests;

    /**
     * Rol del usuario en el sistema.
     * Este campo se almacena como una cadena en la base de datos.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * URL del perfil de LinkedIn del usuario.
     * Este campo es opcional.
     */
    private String linkedinUrl;

    /**
     * Biografía del usuario.
     * Este campo es opcional.
     */
    private String bio;

    /**
     * Fecha y hora de creación del usuario.
     * Representa el momento en que se creó el registro.
     * Formato: yyyy-MM-dd'T'HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Indica si el usuario está activo.
     * Este campo puede ser utilizado para deshabilitar cuentas sin eliminarlas.
     */
    private Boolean isActive;
}