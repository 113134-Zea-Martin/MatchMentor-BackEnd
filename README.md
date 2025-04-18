# Plantilla de Proyecto Spring Boot con Autenticación JWT

Este proyecto proporciona un scaffolding inicial para aplicaciones Java Spring Boot con un sistema de autenticación basado en JWT y gestión de usuarios.

## Características

- Autenticación basada en JWT
- Registro de usuarios
- Login de usuarios
- Sistema de restablecimiento de contraseña
- Envío de correos electrónicos
- Estructura MVC (Model-View-Controller)
- Respuestas API estandarizadas

## Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── scaffold/
│   │           └── template/
│   │               ├── controllers/
│   │               │   └── AuthController.java
│   │               ├── dtos/
│   │               ├── entities/
│   │               │   └── UserEntity.java
│   │               ├── services/
│   │               │   ├── AuthService.java
│   │               │   ├── PasswordResetTokenService.java
│   │               │   └── email/
│   │               │       └── EmailService.java
│   │               └── mappers/
│   └── resources/
│       └── application.properties
```

## Configuración

### Propiedades de JWT
```
jwt.secret=YOUR_SECRET_KEY
jwt.expiration=86400000  # 24 horas en milisegundos
```

### URLs
```
app.url=http://localhost:8080
frontend.url=http://localhost:4200
```

## API Endpoints

### Autenticación

- `POST /api/auth/register/user` - Registro de nuevos usuarios
- `POST /api/auth/login` - Inicio de sesión
- `POST /api/auth/request-reset?email=user@example.com` - Solicitar restablecimiento de contraseña
- `POST /api/auth/reset-password` - Restablecer contraseña

### Administración
- `GET /api/auth/users` - Obtener todos los usuarios (requiere autenticación)

## Requisitos

- Java 17 o superior
- Maven
- Base de datos compatible con JPA

## Cómo iniciar

1. Clonar el repositorio
2. Configurar `application.properties` con datos de conexión a base de datos
3. Ejecutar `mvn clean install`
4. Iniciar la aplicación con `mvn spring-boot:run`

## Seguridad

- Las contraseñas se almacenan cifradas en la base de datos
- Los tokens JWT tienen una expiración configurable
- Sistema de recuperación de contraseña seguro mediante tokens