package com.scaffold.template.services.notification;

import com.scaffold.template.entities.NotificationEntity;
import com.scaffold.template.entities.NotificationType;
import com.scaffold.template.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setup() {
        notificationService = new NotificationServiceImpl(notificationRepository);
    }

    @Test
    void createNotificationConnectionRequestPersistsCorrectNotification() {
        // Arrange
        Long tutorId = 1L;
        String studentName = "Estudiante Ejemplo";
        Long matchId = 100L;

        // Act
        notificationService.createNotificationConnectionRequest(tutorId, studentName, matchId);

        // Assert
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(notificationCaptor.capture());

        NotificationEntity savedNotification = notificationCaptor.getValue();
        assertEquals(tutorId, savedNotification.getUserId());
        assertEquals(NotificationType.NEW_CONNECTION_REQUEST, savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(studentName));
        assertEquals(matchId, savedNotification.getRelatedEntityId());
        assertFalse(savedNotification.getIsRead());
    }

    @Test
    void createNotificationConnectionAnsweredAcceptedPersistsCorrectNotification() {
        // Arrange
        Long studentId = 2L;
        String tutorName = "Tutor Ejemplo";
        Long matchId = 200L;
        boolean isAccepted = true;

        // Act
        notificationService.createNotificationConnectionAnswered(studentId, tutorName, matchId, isAccepted);

        // Assert
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(notificationCaptor.capture());

        NotificationEntity savedNotification = notificationCaptor.getValue();
        assertEquals(studentId, savedNotification.getUserId());
        assertEquals(NotificationType.CONNECTION_ACCEPTED, savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(tutorName));
        assertTrue(savedNotification.getMessage().contains("aceptada"));
        assertEquals(matchId, savedNotification.getRelatedEntityId());
        assertFalse(savedNotification.getIsRead());
    }

    @Test
    void createNotificationConnectionAnsweredRejectedPersistsCorrectNotification() {
        // Arrange
        Long studentId = 2L;
        String tutorName = "Tutor Ejemplo";
        Long matchId = 200L;
        boolean isAccepted = false;

        // Act
        notificationService.createNotificationConnectionAnswered(studentId, tutorName, matchId, isAccepted);

        // Assert
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(notificationCaptor.capture());

        NotificationEntity savedNotification = notificationCaptor.getValue();
        assertEquals(studentId, savedNotification.getUserId());
        assertEquals(NotificationType.CONNECTION_REJECTED, savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(tutorName));
        assertTrue(savedNotification.getMessage().contains("rechazada"));
        assertEquals(matchId, savedNotification.getRelatedEntityId());
        assertFalse(savedNotification.getIsRead());
    }

    @Test
    void createNotificationMeetingRequestPersistsCorrectNotification() {
        // Arrange
        Long studentId = 3L;
        String tutorName = "Tutor Ejemplo";
        Long meetingId = 300L;

        // Act
        notificationService.createNotificationMeetingRequest(studentId, tutorName, meetingId);

        // Assert
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(notificationCaptor.capture());

        NotificationEntity savedNotification = notificationCaptor.getValue();
        assertEquals(studentId, savedNotification.getUserId());
        assertEquals(NotificationType.MEETING_REQUEST, savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(tutorName));
        assertEquals(meetingId, savedNotification.getRelatedEntityId());
        assertFalse(savedNotification.getIsRead());
    }

    @Test
    void createNotificationMeetingAnsweredAcceptedPersistsCorrectNotification() {
        // Arrange
        Long meetingId = 400L;
        Long mentorId = 4L;
        String studentName = "Estudiante Ejemplo";
        boolean isAccepted = true;

        // Act
        notificationService.createNotificationMeetingAnswered(meetingId, mentorId, studentName, isAccepted);

        // Assert
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(notificationCaptor.capture());

        NotificationEntity savedNotification = notificationCaptor.getValue();
        assertEquals(mentorId, savedNotification.getUserId());
        assertEquals(NotificationType.MEETING_ACCEPTED, savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(studentName));
        assertTrue(savedNotification.getMessage().contains("aceptada"));
        assertEquals(meetingId, savedNotification.getRelatedEntityId());
        assertFalse(savedNotification.getIsRead());
    }

    @Test
    void createNotificationMeetingAnsweredRejectedPersistsCorrectNotification() {
        // Arrange
        Long meetingId = 400L;
        Long mentorId = 4L;
        String studentName = "Estudiante Ejemplo";
        boolean isAccepted = false;

        // Act
        notificationService.createNotificationMeetingAnswered(meetingId, mentorId, studentName, isAccepted);

        // Assert
        ArgumentCaptor<NotificationEntity> notificationCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(notificationCaptor.capture());

        NotificationEntity savedNotification = notificationCaptor.getValue();
        assertEquals(mentorId, savedNotification.getUserId());
        assertEquals(NotificationType.MEETING_REJECTED, savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(studentName));
        assertTrue(savedNotification.getMessage().contains("rechazada"));
        assertEquals(meetingId, savedNotification.getRelatedEntityId());
        assertFalse(savedNotification.getIsRead());
    }

    @Test
    void getNotificationsByUserIdReturnsCorrectNotifications() {
        // Arrange
        Long userId = 5L;
        List<NotificationEntity> expectedNotifications = Arrays.asList(
                new NotificationEntity(), new NotificationEntity()
        );
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(expectedNotifications);

        // Act
        List<NotificationEntity> result = notificationService.getNotificationsByUserId(userId);

        // Assert
        assertEquals(expectedNotifications, result);
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    void getTop10UnreadNotificationsByUserIdReturnsCorrectNotifications() {
        // Arrange
        Long userId = 6L;
        List<NotificationEntity> expectedNotifications = Arrays.asList(
                new NotificationEntity(), new NotificationEntity()
        );
        when(notificationRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(expectedNotifications);

        // Act
        List<NotificationEntity> result = notificationService.getTop10UnreadNotificationsByUserId(userId);

        // Assert
        assertEquals(expectedNotifications, result);
        verify(notificationRepository).findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    void markNotificationAsReadUpdatesNotificationCorrectly() {
        // Arrange
        Long notificationId = 7L;
        NotificationEntity notification = new NotificationEntity();
        notification.setIsRead(false);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // Act
        notificationService.markNotificationAsRead(notificationId);

        // Assert
        assertTrue(notification.getIsRead());
        assertNotNull(notification.getReadedAt());
        verify(notificationRepository).save(notification);
    }

    @Test
    void markNotificationAsReadDoesNothingWhenNotificationNotFound() {
        // Arrange
        Long notificationId = 8L;
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act
        notificationService.markNotificationAsRead(notificationId);

        // Assert
        verify(notificationRepository, never()).save(any());
    }
}