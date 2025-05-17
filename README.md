# Plantilla de Proyecto Spring Boot con Autenticación JWT

Este proyecto proporciona un scaffolding completo para aplicaciones Java Spring Boot con sistema de autenticación basado en JWT, gestión de usuarios, sistema de mentoría, pagos y notificaciones.

## Características

- Autenticación basada en JWT
- Registro y login de usuarios
- Sistema de restablecimiento de contraseña
- Envío de correos electrónicos transaccionales
- Estructura MVC (Model-View-Controller)
- Perfiles de usuario (Estudiante/Tutor/Admin)
- Sistema de matches entre estudiantes y tutores
- Gestión de estados de matches (pendiente, aceptado, rechazado)
- Gestión de intereses y áreas de estudio
- Notificaciones internas en la plataforma
- Registro de vistas de perfil entre usuarios
- Programación de reuniones (meetings) entre estudiantes y tutores
- Sistema de pagos para reuniones
- Recordatorios automáticos por email para reuniones del día
- Respuestas API estandarizadas

## Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── scaffold/
│   │           └── template/
│   │               ├── config/
│   │               │   ├── DataInitializer.java
│   │               │   ├── ApplicationConfig.java
│   │               │   ├── CorsConfig.java
│   │               │   ├── PasswordEncoderConfig.java
│   │               │   └── WebConfig.java
│   │               ├── controllers/
│   │               │   ├── AuthController.java
│   │               │   ├── UserController.java
│   │               │   ├── MatchController.java
│   │               │   ├── MeetingController.java
│   │               │   ├── NotificationController.java
│   │               │   ├── PaymentController.java
│   │               │   ├── InterestController.java
│   │               │   ├── AdminController.java
│   │               │   └── ProfileViewController.java
│   │               ├── dtos/
│   │               │   ├── auth/
│   │               │   │   ├── LoginRequestDTO.java
│   │               │   │   ├── LoginResponseDTO.java
│   │               │   │   ├── ResetPasswordRequestDTO.java
│   │               │   │   ├── UserRegisterRequestDTO.java
│   │               │   │   └── UserRegisterResponseDTO.java
│   │               │   ├── profile/
│   │               │   │   ├── UserResponseDTO.java
│   │               │   │   ├── UserUpdateRequestDTO.java
│   │               │   │   ├── StudentResponseDTO.java
│   │               │   │   └── TutorResponseDTO.java
│   │               │   ├── match/
│   │               │   │   ├── MatchRequestDTO.java
│   │               │   │   ├── MatchResponseDTO.java
│   │               │   │   └── ConfirmedMatchResponseDTO.java
│   │               │   ├── meeting/
│   │               │   │   ├── MeetingCreateDTO.java
│   │               │   │   ├── MeetingResponseDTO.java
│   │               │   │   └── MeetingUpdateDTO.java
│   │               │   ├── notification/
│   │               │   │   └── NotificationResponseDTO.java
│   │               │   ├── payment/
│   │               │   │   ├── PaymentCreateDTO.java
│   │               │   │   └── PaymentResponseDTO.java
│   │               │   └── interest/
│   │               │       ├── InterestDTO.java
│   │               │       └── UserInterestDTO.java
│   │               ├── entities/
│   │               │   ├── UserEntity.java
│   │               │   ├── Role.java
│   │               │   ├── MatchEntity.java
│   │               │   ├── Status.java
│   │               │   ├── MeetingEntity.java
│   │               │   ├── PaymentEntity.java
│   │               │   ├── NotificationEntity.java
│   │               │   ├── NotificationType.java
│   │               │   ├── InterestEntity.java
│   │               │   ├── UserInterestEntity.java
│   │               │   ├── UserViewedProfileEntity.java
│   │               │   └── PasswordResetTokenEntity.java
│   │               ├── repositories/
│   │               │   ├── UserRepository.java
│   │               │   ├── MatchRepository.java
│   │               │   ├── MeetingRepository.java
│   │               │   ├── PaymentRepository.java
│   │               │   ├── NotificationRepository.java
│   │               │   ├── InterestRepository.java
│   │               │   ├── UserInterestRepository.java
│   │               │   ├── UserViewedProfileRepository.java
│   │               │   └── PasswordResetTokenRepository.java
│   │               ├── services/
│   │               │   ├── AuthService.java
│   │               │   ├── UserService.java
│   │               │   ├── MatchService.java
│   │               │   ├── MeetingService.java
│   │               │   ├── PaymentService.java
│   │               │   ├── NotificationService.java
│   │               │   ├── InterestService.java
│   │               │   ├── ProfileViewService.java
│   │               │   ├── PasswordResetTokenService.java
│   │               │   └── email/
│   │               │       ├── EmailService.java
│   │               │       └── RecordatorioMeetingService.java
│   │               ├── mappers/
│   │               │   ├── UserMapper.java
│   │               │   ├── MatchMapper.java
│   │               │   ├── MeetingMapper.java
│   │               │   ├── NotificationMapper.java
│   │               │   ├── PaymentMapper.java
│   │               │   └── InterestMapper.java
│   │               ├── security/
│   │               │   ├── JwtAuthenticationFilter.java
│   │               │   ├── JwtService.java
│   │               │   ├── SecurityConfig.java
│   │               │   └── UserSecurityService.java
│   │               ├── exceptions/
│   │               │   ├── GlobalExceptionHandler.java
│   │               │   ├── UserNotFoundException.java
│   │               │   ├── UserAlreadyExistsException.java
│   │               │   ├── MatchNotFoundException.java
│   │               │   ├── MeetingNotFoundException.java
│   │               │   ├── InvalidTokenException.java
│   │               │   ├── TokenExpiredException.java
│   │               │   └── UnauthorizedAccessException.java
│   │               ├── utils/
│   │               │   ├── ApiResponse.java
│   │               │   ├── EmailUtils.java
│   │               │   ├── DateUtils.java
│   │               │   └── TokenUtils.java
│   │               ├── validators/
│   │               │   ├── UserValidator.java
│   │               │   ├── MatchValidator.java
│   │               │   └── MeetingValidator.java
│   │               └── TemplateApplication.java
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

### Configuración de correo electrónico

```
spring.mail.host=smtp.tuservidor.com
spring.mail.port=587
spring.mail.username=tu_usuario
spring.mail.password=tu_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.from=notificaciones@mentormatch.com
```

### Habilitar tareas programadas

En tu clase principal agrega:

```java
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application { ... }
```

## Funcionalidades principales

### Usuarios (`UserEntity`)
- Roles: STUDENT, TUTOR, ADMIN
- Información detallada: intereses, bio, LinkedIn, etc.
- Visibilidad configurable (para tutores)

### Intereses (`InterestEntity`)
- Sistema de etiquetas para relacionar usuarios
- Asociación de usuarios con intereses mediante `UserInterestEntity`

### Matches (`MatchEntity`)
- Conexión entre estudiante y tutor
- Estados: PENDING, ACCEPTED, REJECTED
- Notificaciones automáticas por email

### Reuniones (`MeetingEntity`)
- Programación de sesiones de mentoría
- Datos: fecha, hora, duración, tarifa, razón
- Estados: PROPOSED, CONFIRMED, CANCELLED, etc.

### Pagos (`PaymentEntity`)
- Asociados a reuniones confirmadas
- Estados: PENDING, COMPLETED, FAILED
- Integración con pasarelas de pago (MercadoPago)

### Notificaciones (`NotificationEntity`)
- Sistema interno de notificaciones
- Tipos: solicitud de conexión, match aceptado/rechazado, etc.
- Estado: leídas/no leídas

### Vistas de perfil (`UserViewedProfileEntity`)
- Registro de interacciones entre usuarios
- Analítica de visualizaciones de perfiles

### Correos electrónicos
- Registro exitoso
- Solicitudes de match
- Match aceptado
- Programación de reuniones
- Recordatorios de reuniones diarias

## API Endpoints

### Autenticación
- `POST /api/auth/register/user` - Registro de nuevos usuarios
- `POST /api/auth/login` - Inicio de sesión
- `POST /api/auth/request-reset?email=user@example.com` - Solicitar restablecimiento
- `POST /api/auth/reset-password` - Restablecer contraseña

### Usuarios
- `GET /api/users/{profileId}` - Obtener perfil de usuario
- `PUT /api/users/{profileId}` - Actualizar perfil de usuario
- `GET /api/users/me` - Obtener información del usuario autenticado
- `GET /api/users/me/roles` - Obtener información específica según el rol

### Administración
- `GET /api/auth/users` - Obtener todos los usuarios (admin)

### Intereses
- `GET /api/interests` - Listar todos los intereses disponibles
- `POST /api/users/interests` - Agregar intereses al usuario actual

### Matches
- `POST /api/matches/request/{tutorId}` - Solicitar match con un tutor
- `GET /api/matches/pending` - Obtener matches pendientes
- `GET /api/matches/confirmed` - Obtener matches confirmados
- `PUT /api/matches/{matchId}/confirm` - Confirmar un match pendiente
- `PUT /api/matches/{matchId}/reject` - Rechazar un match pendiente

### Meetings
- `POST /api/meetings` - Crear una reunión
- `GET /api/meetings/{id}` - Obtener detalles de una reunión
- `PUT /api/meetings/{id}/confirm` - Confirmar una reunión
- `PUT /api/meetings/{id}/cancel` - Cancelar una reunión

### Pagos
- `POST /api/payments` - Registrar un pago para una reunión
- `GET /api/payments/{id}` - Obtener detalles de un pago
- `POST /api/payments/webhook` - Endpoint para webhooks de la pasarela de pago

### Notificaciones
- `GET /api/notifications` - Listar notificaciones del usuario
- `PUT /api/notifications/{id}/read` - Marcar notificación como leída

### Vistas de perfil
- `POST /api/profiles/{id}/view` - Registrar vista de perfil

## Recordatorios automáticos de reuniones

El sistema envía un email diario a las 06:10 AM a todos los usuarios que tienen reuniones programadas para ese día. La funcionalidad está implementada en `RecordatorioMeetingService` usando `@Scheduled` y `@Transactional` para evitar problemas de carga perezosa (LazyInitializationException).

## Tipos de emails automáticos

- **Registro**: Confirmación al registrarse
- **Solicitud de match**: Notifica al tutor cuando recibe una solicitud
- **Match aceptado**: Notifica al estudiante cuando su solicitud es aceptada
- **Reunión programada**: Notifica al estudiante cuando el tutor programa una reunión
- **Respuesta a reunión**: Notifica al tutor cuando el estudiante responde a la solicitud
- **Recordatorio de reunión**: Alerta a ambas partes sobre reuniones del día

## Requisitos

- Java 17 o superior
- Maven
- Base de datos compatible con JPA
- Servidor SMTP para envío de correos

## Cómo iniciar

1. Clonar el repositorio
2. Configurar `application.properties` con datos de conexión a base de datos y correo
3. Ejecutar `mvn clean install`
4. Iniciar la aplicación con `mvn spring-boot:run`

## Población inicial de datos

El proyecto incluye un `DataInitializer` que crea datos de ejemplo:
- Intereses predefinidos
- Usuario administrador
- Estudiantes y tutores con perfiles completos
- Matches en diferentes estados
- Notificaciones
- Vistas de perfil
- Reuniones programadas
- Pagos de ejemplo

## Seguridad

- Las contraseñas se almacenan cifradas en la base de datos
- Los tokens JWT tienen una expiración configurable
- Sistema de recuperación de contraseña seguro mediante tokens
- Las transacciones de pago se manejan de forma segura