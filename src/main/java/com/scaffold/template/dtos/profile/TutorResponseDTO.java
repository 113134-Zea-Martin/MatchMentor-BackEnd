package com.scaffold.template.dtos.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TutorResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
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

    private List<String> interests;

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
