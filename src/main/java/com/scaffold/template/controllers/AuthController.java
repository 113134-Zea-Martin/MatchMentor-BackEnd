package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.auth.PasswordResetRequestDTO;
import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.UserRegisterResponseDTO;
import com.scaffold.template.dtos.auth.login.LoginRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginResponseDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.services.AuthService;
import com.scaffold.template.services.PasswordResetTokenService;
import com.scaffold.template.services.email.EmailService;
import com.scaffold.template.services.mappers.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para gestionar las operaciones relacionadas con la autenticación y el registro de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${app.url}")
    private String appUrl;

    @Value("${frontend.url}")
    private String frontendUrl;

    private final EmailService emailService;

    private final AuthService authService;

    private final PasswordResetTokenService passwordResetTokenService;

    /**
     * Constructor para inyectar el servicio de autenticación.
     *
     * @param authService Servicio de autenticación que contiene la lógica de negocio.
     */
    @Autowired
    public AuthController(AuthService authService, EmailService emailService
            , PasswordResetTokenService passwordResetTokenService) {
        this.authService = authService;
        this.emailService = emailService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    /**
     * Endpoint para obtener todos los usuarios registrados.
     *
     * @return Una respuesta HTTP con la lista de todos los usuarios.
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        try {
            List<UserResponseDTO> users = authService.getAllUsers();
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(users);
            response.setMessage("Usuarios obtenidos correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al obtener los usuarios: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Endpoint para registrar un nuevo estudiante.
     *
     * @param userRegisterRequestDTO Objeto que contiene los datos del usuario a registrar.
     * @return Una respuesta HTTP con los datos del usuario registrado o un mensaje de error si ocurre un problema.
     */
    @PostMapping("/register/user")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        try {
            UserEntity userEntity = authService.registerUser(userRegisterRequestDTO);
            UserRegisterResponseDTO userRegisterResponseDTO = UserMapper.toResponseDTO(userEntity);

            response.setSuccess(true);
            response.setStatusCode(201);
            response.setData(userRegisterResponseDTO);
            response.setMessage("Usuario registrado correctamente");

            // Enviar correo electrónico de registro exitoso
            emailService.sendSuccessfulRegistrationEmail(userRegisterRequestDTO);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error interno al registrar usuario: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        try {
            LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);

            if (!loginResponseDTO.isActive()) {
                response.setSuccess(false);
                response.setStatusCode(403);
                response.setMessage("Usuario inactivo");
                return ResponseEntity.ok(response);
            }

            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(loginResponseDTO);
            response.setMessage("Login exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(401);
            response.setMessage("Credenciales inválidas");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/request-reset")
    public ResponseEntity<ApiResponse> requestReset(@RequestParam String email) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        try {
            UserEntity user = authService.getUserByEmail(email);

            String token = passwordResetTokenService.createTokenForUser(user);
            String resetLink = frontendUrl + "/auth/reset-password?token=" + token;
            String subject = "Restablecimiento de contraseña";
            String body = "Hola " + user.getFirstName() + ",\n\n" +
                    "Hemos recibido una solicitud para restablecer tu contraseña.\n" +
                    "Haz clic en el siguiente enlace para restablecerla:\n" +
                    resetLink + "\n\n" +
                    "Si no solicitaste este cambio, ignora este correo.\n\n" +
                    "Saludos,\nEl equipo de Mentor Match";
            emailService.sendMail(email, subject, body);

            response.setStatusCode(200);
            response.setData(token);
            response.setSuccess(true);
            response.setMessage("Se ha enviado un correo electrónico con el enlace para restablecer la contraseña");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetRequestDTO resetRequest) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        try {
            boolean success = passwordResetTokenService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());

            if (success) {
                response.setSuccess(true);
                response.setStatusCode(200);
                response.setMessage("Contraseña restablecida correctamente");
            } else {
                response.setSuccess(false);
                response.setStatusCode(400);
                response.setMessage("Token inválido o expirado");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al restablecer la contraseña: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

}
