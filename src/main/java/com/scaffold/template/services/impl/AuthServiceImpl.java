package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginResponseDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserInterestEntity;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.security.JwtConfig;
import com.scaffold.template.services.AuthService;
import com.scaffold.template.services.userActivity.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final UserActivityService userActivityService;

    /**
     * Constructor que inyecta el repositorio de usuarios y el codificador de contraseñas.
     *
     * @param userRepository el repositorio de usuarios
     * @param passwordEncoder el codificador de contraseñas
     */
    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtConfig jwtConfig, UserActivityService userActivityService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.userActivityService = userActivityService;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * @return una lista de entidades de usuario
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();

        for (UserEntity user : users) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setId(user.getId());
            userResponseDTO.setFirstName(user.getFirstName());
            userResponseDTO.setLastName(user.getLastName());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setRole(user.getRole());
            userResponseDTO.setIsActive(user.getIsActive());
            userResponseDTOList.add(userResponseDTO);
        }

        return userResponseDTOList;
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

        if (userRegisterRequestDTO.getPassword() == null || userRegisterRequestDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }


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

        // Asignar los intereses del usuario
        List<UserInterestEntity> userInterestEntityList = new ArrayList<>();

        userEntity.setUserInterests(userInterestEntityList);
        userEntity.setRole(Role.STUDENT); // Asignar el rol de estudiante por defecto
        userEntity.setCreatedAt(LocalDateTime.now()); // Establecer la fecha de creación
        userEntity.setIsActive(true); // Establecer el estado activo del usuario
        userEntity.setIsVisible(false); // Establecer el estado visible del usuario

        // Guardar la entidad de usuario en la base de datos
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity authenticateUser(String email, String password) {
        // Verificar si el usuario existe
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        //Verificar estado de cuenta activo
        if (!userEntity.getIsActive()) {
            throw new IllegalArgumentException("Cuenta inactiva.");
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


        UserEntity user = userRepository.findByEmail(loginRequestDTO.getEmail());

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setId(user.getId());
        loginResponseDTO.setFirstName(user.getFirstName());
        loginResponseDTO.setLastName(user.getLastName());
        loginResponseDTO.setEmail(user.getEmail());
        loginResponseDTO.setRole(user.getRole());
        loginResponseDTO.setActive(user.getIsActive());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        claims.put("isActive", user.getIsActive());

        String token = jwtConfig.generateToken(claims, userDetails);

        loginResponseDTO.setToken(token);

        userActivityService.createUserActivity(user.getId(), "LOGIN");

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