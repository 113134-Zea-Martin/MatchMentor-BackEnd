package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.entities.NotificationEntity;
import com.scaffold.template.services.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getNotifications(@PathVariable("userId") String userId,
                                                        @RequestParam(required = true) Boolean isTop10) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setTimestamp(LocalDateTime.now());

        try {
            if (isTop10) {
                List<NotificationEntity> notifications = notificationService.getTop10UnreadNotificationsByUserId(Long.parseLong(userId));
                apiResponse.setData(notifications);
                apiResponse.setMessage("Notificaciones obtenidas correctamente");
                apiResponse.setStatusCode(200);
                apiResponse.setSuccess(true);
            } else {
                List<NotificationEntity> notifications = notificationService.getNotificationsByUserId(Long.parseLong(userId));
                apiResponse.setData(notifications);
                apiResponse.setMessage("Notificaciones obtenidas correctamente");
                apiResponse.setStatusCode(200);
                apiResponse.setSuccess(true);
            }
        } catch (Exception e) {
            apiResponse.setMessage("Error al obtener las notificaciones: " + e.getMessage());
            apiResponse.setSuccess(false);
            apiResponse.setStatusCode(500);
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markNotificationAsRead(@PathVariable("notificationId") Long notificationId) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setTimestamp(LocalDateTime.now());

        try {
            notificationService.markNotificationAsRead(notificationId);
            apiResponse.setMessage("Notificación marcada como leída correctamente");
            apiResponse.setStatusCode(200);
            apiResponse.setSuccess(true);
        } catch (Exception e) {
            apiResponse.setMessage("Error al marcar la notificación como leída: " + e.getMessage());
            apiResponse.setSuccess(false);
            apiResponse.setStatusCode(500);
        }
        return ResponseEntity.ok(apiResponse);
    }

}
