package com.scaffold.template.services.notification;

import com.scaffold.template.entities.NotificationEntity;
import com.scaffold.template.entities.NotificationType;
import com.scaffold.template.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
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
    }
}
