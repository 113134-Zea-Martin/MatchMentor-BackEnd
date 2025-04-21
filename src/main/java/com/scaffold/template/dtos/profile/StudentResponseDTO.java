package com.scaffold.template.dtos.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
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
