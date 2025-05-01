package com.scaffold.template.services.notification;

import com.scaffold.template.entities.NotificationEntity;
import com.scaffold.template.entities.NotificationType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface NotificationService {
    void createNotificationConnectionRequest(Long tutorId, String studentName, Long matchId);
}
