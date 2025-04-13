package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.UserRegisterRequestDTO;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.models.Dummy;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Constructor que inyecta el repositorio de usuarios y el codificador de contraseñas.
     *
     * @param userRepository el repositorio de usuarios
     * @param passwordEncoder el codificador de contraseñas
     */
    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
     * Registra un nuevo estudiante en el sistema.
     * Codifica la contraseña proporcionada, crea una nueva entidad de usuario con los datos del DTO,
     * asigna el rol de estudiante y guarda la entidad en la base de datos.
     *
     * @param userRegisterRequestDTO DTO que contiene los datos del estudiante a registrar
     * @return la entidad del usuario registrado
     */
    @Override
    public UserEntity registerStudent(UserRegisterRequestDTO userRegisterRequestDTO) {

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
        userEntity.setRole(Role.STUDENT); // Asignar el rol de estudiante
        userEntity.setCreatedAt(LocalDateTime.now()); // Establecer la fecha de creación
        userEntity.setIsActive(true); // Establecer el estado activo del usuario

        // Guardar la entidad de usuario en la base de datos
        return userRepository.save(userEntity);
    }
}