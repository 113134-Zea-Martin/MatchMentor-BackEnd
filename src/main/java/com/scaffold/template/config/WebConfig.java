package com.scaffold.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Spring MVC para personalizar el comportamiento del framework web.
 * Esta clase implementa la interfaz {@link WebMvcConfigurer} para proporcionar configuraciones específicas.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Tiempo máximo en segundos para que las respuestas CORS sean almacenadas en caché por los navegadores.
     */
    private static final int MAXAGE = 3600;

    /**
     * Configura los mapeos de CORS (Cross-Origin Resource Sharing) para permitir solicitudes desde cualquier origen.
     *
     * @param registry el registro de configuraciones CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todas las rutas.
                .allowedOriginPatterns("*") // Permite solicitudes desde cualquier origen.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos.
                .allowedHeaders("*") // Permite todos los encabezados.
                .exposedHeaders("Access-Control-Allow-Origin") // Expone encabezados específicos al cliente.
                .allowCredentials(false) // No permite el uso de credenciales en solicitudes CORS.
                .maxAge(MAXAGE); // Tiempo máximo en caché para las respuestas CORS.
    }
}