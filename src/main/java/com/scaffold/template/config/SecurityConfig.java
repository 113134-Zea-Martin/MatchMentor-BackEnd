package com.scaffold.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de seguridad para el sistema.
 * Proporciona configuraciones relacionadas con la codificación de contraseñas.
 */
@Configuration
public class SecurityConfig {

    /**
     * Define un bean para el codificador de contraseñas.
     * Utiliza BCrypt para codificar las contraseñas de manera segura.
     *
     * @return una instancia de {@link PasswordEncoder} basada en BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}