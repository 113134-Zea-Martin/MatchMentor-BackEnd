package com.scaffold.template.entities;

public enum NotificationType {
    NEW_CONNECTION_REQUEST, //El estudiante ha hecho clic en el botón "Conectar" en el perfil de un tutor.
    CONNECTION_ACCEPTED, //El tutor ha aceptado la solicitud de conexión del estudiante.
    CONNECTION_REJECTED, //El tutor ha rechazado la solicitud de conexión del estudiante.
    NEW_MESSAGE, //Un estudiante o un tutor envía un nuevo mensaje a través del sistema de chat de la plataforma.
    MENTORSHIP_REMINDER, //Recordatorio de la próxima sesión de mentoría.
}
