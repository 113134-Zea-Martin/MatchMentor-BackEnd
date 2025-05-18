package com.scaffold.template.services.notification;

import com.scaffold.template.entities.NotificationEntity;
import com.scaffold.template.entities.NotificationType;
import com.scaffold.template.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
//    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
//        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void createNotificationConnectionRequest(Long tutorId, String studentName, Long matchId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(tutorId);
        notification.setNotificationType(NotificationType.NEW_CONNECTION_REQUEST);
        notification.setMessage("Tienes una nueva solicitud de match de " + studentName + "!");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setRelatedEntityId(matchId);
        notificationRepository.save(notification);

        // Enviar la notificación al cliente a través de WebSocket
//        messagingTemplate.convertAndSend("/topic/notifications/" + tutorId, notification);
    }

    @Override
    public void createNotificationConnectionAnswered(Long studentId, String tutorName, Long matchId, boolean isAccepted) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(studentId);
        notification.setNotificationType(isAccepted ? NotificationType.CONNECTION_ACCEPTED : NotificationType.CONNECTION_REJECTED);
        notification.setMessage("Tu solicitud de match con " + tutorName + (isAccepted ? " ha sido aceptada!" : " ha sido rechazada!"));
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setRelatedEntityId(matchId);
        notificationRepository.save(notification);
        // Enviar la notificación al cliente a través de WebSocket
//        messagingTemplate.convertAndSend("/topic/notifications/" + studentId, notification);
    }

    @Override
    public void createNotificationMeetingRequest(Long studentId, String tutorName, Long meetingId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(studentId);
        notification.setNotificationType(NotificationType.MEETING_REQUEST);
        notification.setMessage("Tienes una nueva solicitud de reunión con " + tutorName + "!");
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setRelatedEntityId(meetingId);
        notificationRepository.save(notification);
        // Enviar la notificación al cliente a través de WebSocket
//        messagingTemplate.convertAndSend("/topic/notifications/" + studentId, notification);
    }

    @Override
    public void createNotificationMeetingAnswered(Long meetingId, Long mentorId, String studentName, boolean isAccepted) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUserId(mentorId);
        notification.setNotificationType(isAccepted ? NotificationType.MEETING_ACCEPTED : NotificationType.MEETING_REJECTED);
        notification.setMessage("La solicitud de reunión con " + studentName + (isAccepted ? " ha sido aceptada!" : " ha sido rechazada!"));
        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);
        notification.setRelatedEntityId(meetingId);
        notificationRepository.save(notification);
        // Enviar la notificación al cliente a través de WebSocket
//        messagingTemplate.convertAndSend("/topic/notifications/" + mentorId, notification);
    }

    @Override
    public List<NotificationEntity> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

//    @Override
//    public boolean hasUnreadNotifications(Long userId) {
//        return notificationRepository.existsByUserIdAndIsReadFalse(userId);
//    }

    @Override
    public List<NotificationEntity> getTop10UnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }


    @Override
    public void markNotificationAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            notification.setReadedAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }


}
