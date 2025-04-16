package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginResponseDTO;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.security.JwtConfig;
import com.scaffold.template.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio de autenticación.
 * Proporciona la lógica para manejar las operaciones relacionadas con la autenticación y registro de usuarios.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Repositorio para realizar operaciones CRUD sobre la entidad de usuario.
     */
    private final UserRepository userRepository;

    /**
     * Codificador de contraseñas para garantizar la seguridad de las contraseñas almacenadas.
     */
    private final PasswordEncoder passwordEncoder;


    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    /**
     * Constructor que inyecta el repositorio de usuarios y el codificador de contraseñas.
     *
     * @param userRepository el repositorio de usuarios
     * @param passwordEncoder el codificador de contraseñas
     */
    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return una lista de entidades de usuario
     */
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Codifica la contraseña proporcionada, crea una nueva entidad de usuario con los datos del DTO,
     * asigna el rol de estudiante y guarda la entidad en la base de datos.
     *
     * @param userRegisterRequestDTO DTO que contiene los datos del usuario a registrar
     * @return la entidad del usuario registrado
     */
    @Override
    public UserEntity registerUser(UserRegisterRequestDTO userRegisterRequestDTO) {

        // Verificar si el correo electrónico ya está registrado
        if (userRepository.existsByEmail(userRegisterRequestDTO.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        // Codificar la contraseña del usuario
        String encodedPassword = passwordEncoder.encode(userRegisterRequestDTO.getPassword());

        // Crear una nueva entidad de usuario a partir del DTO
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRegisterRequestDTO.getFirstName());
        userEntity.setLastName(userRegisterRequestDTO.getLastName());
        userEntity.setEmail(userRegisterRequestDTO.getEmail());
        userEntity.setPassword(encodedPassword);
        userEntity.setInterests(userRegisterRequestDTO.getInterests());
        userEntity.setRole(Role.STUDENT); // Asignar el rol de estudiante por defecto
        userEntity.setCreatedAt(LocalDateTime.now()); // Establecer la fecha de creación
        userEntity.setIsActive(true); // Establecer el estado activo del usuario

        // Guardar la entidad de usuario en la base de datos
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity authenticateUser(String email, String password) {
        // Verificar si el usuario existe
        UserEntity userEntity = userRepository.findByEmail(email);

        //Verificar estado de cuenta activo
        if (!userEntity.getIsActive()) {
            throw new IllegalArgumentException("Cuenta inactiva.");
        }

        if (userEntity == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        return userEntity;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        User userDetails = (User) authentication.getPrincipal();
        String token = jwtConfig.generateToken(userDetails);

        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail());

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(user.getId());
        loginResponseDTO.setFirstName(user.getFirstName());
        loginResponseDTO.setLastName(user.getLastName());
        loginResponseDTO.setEmail(user.getEmail());
        loginResponseDTO.setRole(user.getRole());
        loginResponseDTO.setActive(user.getIsActive());
        loginResponseDTO.setToken(token);

        return loginResponseDTO;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        // Verificar si el usuario existe
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        return userEntity;
    }
}