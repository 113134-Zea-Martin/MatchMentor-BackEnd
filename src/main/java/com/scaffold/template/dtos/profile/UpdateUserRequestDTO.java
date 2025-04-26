package com.scaffold.template.dtos.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para actualizar la información de un usuario.
 * Contiene campos comunes y específicos para estudiantes y tutores.
 */
@Data
public class UpdateUserRequestDTO {

    /**
     * Nombre del usuario.
     * No puede estar vacío.
     */
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String firstName;

    /**
     * Apellido del usuario.
     * No puede estar vacío.
     */
    @NotEmpty(message = "El apellido no puede estar vacío")
    private String lastName;

    /**
     * Correo electrónico del usuario.
     * No puede estar vacío y debe ser válido.
     */
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    /**
     * Fecha de nacimiento del usuario.
     * Formato: yyyy-MM-dd.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
     * No puede ser un valor negativo.
     * Campo opcional.
     */
    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
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

    /**
     * Indica si el perfil del usuario es visible.
     * Campo opcional.
     */
    private Boolean isVisible;

    // Campos comunes para estudiantes y tutores

    /**
     * Lista de intereses del usuario.
     * Campo opcional.
     */
    private List<String> interests;

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
     * Indica si el usuario está activo.
     * Puede ser utilizado para deshabilitar cuentas sin eliminarlas.
     */
    private Boolean isActive;
}