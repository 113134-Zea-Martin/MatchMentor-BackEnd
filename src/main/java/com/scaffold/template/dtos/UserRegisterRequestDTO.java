package com.scaffold.template.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para registrar un estudiante.
 * Esta clase encapsula los datos necesarios para registrar un estudiante en el sistema.
 */
@Data
public class UserRegisterRequestDTO {

    /**
     * Nombre del estudiante.
     */
    @NotNull(message = "El nombre no puede ser nulo")
    private String firstName;

    /**
     * Apellido del estudiante.
     */
    @NotNull(message = "El apellido no puede ser nulo")
    private String lastName;

    /**
     * Correo electrónico del estudiante.
     * Debe ser único y válido.
     */
    @NotNull(message = "El correo electrónico no puede ser nulo")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    /**
     * Contraseña del estudiante.
     * Utilizada para la autenticación.
     */
    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    /**
     * Intereses del estudiante.
     * Campo opcional que describe los intereses personales del estudiante.
     */
    private String interests;
}