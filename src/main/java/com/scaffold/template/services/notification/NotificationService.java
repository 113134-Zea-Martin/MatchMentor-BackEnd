package com.scaffold.template.services.notification;

import com.scaffold.template.entities.NotificationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Obtener las notificaciones por usuario ordenadas por fecha de creación (las más recientes primero)
    List<NotificationEntity> getNotificationsByUserId(Long userId);

    // Devuelve true si el usuario tiene notificaciones no leídas
//    boolean hasUnreadNotifications(Long userId);

    // Devuelve las ultimas 10 notificaciones no leídas ordenadas por fecha de creación (las más recientes primero)
    List<NotificationEntity> getTop10UnreadNotificationsByUserId(Long userId);

    void markNotificationAsRead(Long notificationId); // Marca una notificación como leída

    boolean hasUnreadNotifications(Long userId); // Devuelve true si el usuario tiene notificaciones no leídas
}
