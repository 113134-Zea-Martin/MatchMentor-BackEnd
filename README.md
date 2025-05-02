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
- Perfiles de usuario (Estudiante/Tutor)
- Sistema de matches entre estudiantes y tutores
- Gestión de estados de matches (pendiente, confirmado, rechazado)

## Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── scaffold/
│   │           └── template/
│   │               ├── controllers/
│   │               │   ├── AuthController.java
│   │               │   ├── UserController.java
│   │               │   └── MatchController.java
│   │               ├── dtos/
│   │               │   ├── profile/
│   │               │   │   ├── UserResponseDTO.java
│   │               │   │   ├── StudentResponseDTO.java
│   │               │   │   └── TutorResponseDTO.java
│   │               │   └── match/
│   │               │       └── ConfirmedMatchResponseDTO.java
│   │               ├── entities/
│   │               │   ├── UserEntity.java
│   │               │   ├── MatchEntity.java
│   │               │   └── Status.java
│   │               ├── repositories/
│   │               │   ├── UserRepository.java
│   │               │   └── MatchRepository.java
│   │               ├── services/
│   │               │   ├── AuthService.java
│   │               │   ├── UserService.java
│   │               │   ├── MatchService.java
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

### Usuarios

- `GET /api/users/{profileId}` - Obtener perfil de usuario por ID
- `GET /api/users/me` - Obtener información del usuario autenticado
- `GET /api/users/me/roles` - Obtener información específica según el rol del usuario (Estudiante/Tutor)

### Administración
- `GET /api/auth/users` - Obtener todos los usuarios (requiere autenticación)

### Matches

- `POST /api/matches/request/{tutorId}` - Solicitar match con un tutor específico
- `GET /api/matches/pending` - Obtener matches pendientes para el usuario autenticado
- `GET /api/matches/confirmed` - Obtener matches confirmados para el usuario autenticado
- `PUT /api/matches/{matchId}/confirm` - Confirmar un match pendiente
- `PUT /api/matches/{matchId}/reject` - Rechazar un match pendiente

## Modelos principales

### Usuario (UserEntity)
Representa a los usuarios del sistema con sus roles específicos (STUDENT, TUTOR).

### Match (MatchEntity)
Representa la conexión entre un estudiante y un tutor:
- Atributos principales: student, tutor, status, createdAt, updatedAt
- Estados posibles (Status): PENDING, CONFIRMED, REJECTED

### Respuesta de Match Confirmado (ConfirmedMatchResponseDTO)
DTO que traslada la información de matches confirmados entre las capas de la aplicación:
- Incluye: id, userId, fullName, role, description, updatedAt, isActive

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