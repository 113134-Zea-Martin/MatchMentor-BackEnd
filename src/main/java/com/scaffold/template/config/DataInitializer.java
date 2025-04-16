package com.scaffold.template.config;

import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar si ya existen usuarios para evitar duplicados
            if (userRepository.count() == 0) {
                // Crear usuario administrador
                UserEntity admin = new UserEntity();
                admin.setFirstName("Admin");
                admin.setLastName("Sistema");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("password123"));
                admin.setRole(Role.ADMIN);
                admin.setIsActive(true);
                admin.setCreatedAt(LocalDateTime.now());
                userRepository.save(admin);

                // Crear un usuario normal
                UserEntity user = new UserEntity();
                user.setFirstName("Usuario");
                user.setLastName("Normal");
                user.setEmail("martincho370@gmail.com");
                user.setPassword(passwordEncoder.encode("password123"));
                user.setRole(Role.STUDENT);
                user.setIsActive(true);
                user.setCreatedAt(LocalDateTime.now());
                userRepository.save(user);

                // Crear un usuario inactivo
                UserEntity userInactivo = new UserEntity();
                userInactivo.setFirstName("Usuario");
                userInactivo.setLastName("Inactivo");
                userInactivo.setEmail("inactivo@example.com");
                userInactivo.setPassword(passwordEncoder.encode("password123"));
                userInactivo.setRole(Role.STUDENT);
                userInactivo.setIsActive(false);
                userInactivo.setCreatedAt(LocalDateTime.now());
                userRepository.save(userInactivo);

                System.out.println("Datos iniciales cargados correctamente");
            }
        };
    }
}