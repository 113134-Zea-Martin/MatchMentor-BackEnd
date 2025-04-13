package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.UserRegisterRequestDTO;
import com.scaffold.template.dtos.UserRegisterResponseDTO;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.services.AuthService;
import com.scaffold.template.services.mappers.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para gestionar las operaciones relacionadas con la autenticación y el registro de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * Constructor para inyectar el servicio de autenticación.
     *
     * @param authService Servicio de autenticación que contiene la lógica de negocio.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para obtener todos los usuarios registrados.
     *
     * @return Una respuesta HTTP con la lista de todos los usuarios.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    /**
     * Endpoint para registrar un nuevo estudiante.
     *
     * @param userRegisterRequestDTO Objeto que contiene los datos del usuario a registrar.
     * @return Una respuesta HTTP con los datos del usuario registrado o un mensaje de error si ocurre un problema.
     */
    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            UserEntity userEntity = authService.registerStudent(userRegisterRequestDTO);
            UserRegisterResponseDTO userRegisterResponseDTO = UserMapper.toResponseDTO(userEntity);
            response.setSuccess(true);
            response.setData(userRegisterResponseDTO);
            response.setMessage("Estudiante registrado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
