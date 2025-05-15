package com.scaffold.template.services.notification;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    void createNotificationConnectionRequest(Long tutorId, String studentName, Long matchId); // Crea una notificación
    // de solicitud de conexión para el tutor.
    void createNotificationConnectionAnswered(Long studentId, String tutorName, Long matchId, boolean isAccepted); // Crea una notificación
    // de respuesta a la solicitud de conexión para el estudiante.

    void createNotificationMeetingRequest(Long studentId, String tutorName, Long meetingId); // Crea una notificación
    // de solicitud de reunión para el estudiante.

    void createNotificationMeetingAnswered(Long meetingId, Long mentorId, String studentName, boolean isAccepted); // Crea una notificación
    // de respuesta a la solicitud de reunión para el mentor.
}
