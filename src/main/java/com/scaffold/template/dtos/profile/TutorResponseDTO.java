package com.scaffold.template.dtos.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para representar la información de un tutor.
 * Contiene campos específicos para tutores y campos comunes para estudiantes y tutores.
 */
@Data
public class TutorResponseDTO {

    /**
     * Identificador único del tutor.
     */
    private Long id;

    /**
     * Nombre del tutor.
     */
    private String firstName;

    /**
     * Apellido del tutor.
     */
    private String lastName;

    /**
     * Correo electrónico del tutor.
     */
    private String email;

    /**
     * Fecha de nacimiento del tutor.
     * Formato: yyyy-MM-dd.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    /**
     * Ubicación del tutor.
     * Campo opcional.
     */
    private String location;

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
     * Lista de intereses del tutor.
     * Campo opcional.
     */
    private List<String> interests;

    /**
     * URL del perfil de LinkedIn del tutor.
     * Campo opcional.
     */
    private String linkedinUrl;

    /**
     * Biografía del tutor.
     * Campo opcional.
     */
    private String bio;

    /**
     * Fecha y hora de creación del tutor.
     * Representa el momento en que se creó el registro.
     * Formato: yyyy-MM-dd'T'HH:mm:ss.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Indica si el tutor está activo.
     * Puede ser utilizado para deshabilitar cuentas sin eliminarlas.
     */
    private Boolean isActive;

    /**
     * Indica si el perfil del tutor es visible para otros usuarios.
     * Campo opcional.
     */
    private Boolean isVisible;
}