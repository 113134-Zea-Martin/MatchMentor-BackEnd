package com.scaffold.template.services.notification;

import com.scaffold.template.entities.NotificationEntity;
import com.scaffold.template.entities.NotificationType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface NotificationService {
    void createNotificationConnectionRequest(Long tutorId, String studentName, Long matchId); // Crea una notificación
    // de solicitud de conexión para el tutor.
    void createNotificationConnectionAnswered(Long studentId, String tutorName, Long matchId, boolean isAccepted); // Crea una notificación
    // de respuesta a la solicitud de conexión para el estudiante.

    void createNotificationMeetingRequest(Long studentId, String tutorName, Long matchId); // Crea una notificación
}
