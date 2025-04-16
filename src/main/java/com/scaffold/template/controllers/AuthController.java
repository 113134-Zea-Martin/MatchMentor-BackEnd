package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.auth.PasswordResetRequestDTO;
import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.UserRegisterResponseDTO;
import com.scaffold.template.dtos.auth.login.LoginRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginResponseDTO;
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
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
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
            response.setData(userRegisterResponseDTO);
            response.setMessage("Usuario registrado correctamente");

            // Enviar correo electrónico de registro exitoso
            emailService.sendSuccessfulRegistrationEmail(userRegisterRequestDTO);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
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
                response.setMessage("Usuario inactivo");
                return ResponseEntity.badRequest().body(response);
            }

            response.setSuccess(true);
            response.setData(loginResponseDTO);
            response.setMessage("Login exitoso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Credenciales inválidas");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/request-reset")
    public ResponseEntity<ApiResponse> requestReset(@RequestParam String email) {
        UserEntity user = authService.getUserByEmail(email);
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        if (user == null) {
            response.setSuccess(false);
            response.setMessage("El correo electrónico no está registrado");
            return ResponseEntity.status(404).body(response);
        }

        String token = passwordResetTokenService.createTokenForUser(user);

//        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        String subject = "Restablecimiento de contraseña";
        String body = "Hola " + user.getFirstName() + ",\n\n" +
                "Hemos recibido una solicitud para restablecer tu contraseña.\n" +
                "Haz clic en el siguiente enlace para restablecerla:\n" +
                resetLink + "\n\n" +
                "Si no solicitaste este cambio, ignora este correo.\n\n" +
                "Saludos,\nEl equipo de Mentor Match";
        emailService.sendMail(email, subject, body);
        response.setData(token);
        response.setSuccess(true);
        response.setMessage("Se ha enviado un correo electrónico con el enlace para restablecer la contraseña");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody PasswordResetRequestDTO resetRequest) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());

        boolean success = passwordResetTokenService.resetPassword(resetRequest.getToken(), resetRequest.getNewPassword());
        response.setSuccess(success);
        response.setMessage(success ? "Contraseña restablecida correctamente" : "Token inválido o expirado");
        return ResponseEntity.ok(response);
    }

}
