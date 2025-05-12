package com.scaffold.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Spring MVC para personalizar el comportamiento del framework web.
 * Esta clase implementa la interfaz {@link WebMvcConfigurer} para proporcionar configuraciones específicas.
 */
@Configuration
@EnableAsync
public class WebConfig implements WebMvcConfigurer {


    @Value("${mercadopago.redirect.url.origin}")
    private String url;

    /**
     * Tiempo máximo en segundos para que las respuestas CORS sean almacenadas en caché por los navegadores.
     */
    private static final int MAXAGE = 3600;

    /**
     * Configura los mapeos de CORS (Cross-Origin Resource Sharing) para permitir solicitudes desde cualquier origen.
     *
     * @param registry el registro de configuraciones CORS
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Permite CORS para todas las rutas.
//                .allowedOriginPatterns("*") // Permite solicitudes desde cualquier origen.
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos.
//                .allowedHeaders("*") // Permite todos los encabezados.
//                .exposedHeaders("Access-Control-Allow-Origin") // Expone encabezados específicos al cliente.
//                .allowCredentials(true) // No permite el uso de credenciales en solicitudes CORS.
//                .maxAge(MAXAGE); // Tiempo máximo en caché para las respuestas CORS.
//    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String localUrl = "http://localhost:4200";
        String ngrokUrl = url;
        registry.addMapping("/**")
                .allowedOrigins(ngrokUrl, localUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin", "Authorization")
                .allowCredentials(true)
                .maxAge(MAXAGE);
    }
}