package com.scaffold.template.repositories;

import com.scaffold.template.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para encontrar notificaciones por tipo o usuario
    // List<NotificationEntity> findByType(NotificationType type);
    // List<NotificationEntity> findByUserId(Long userId);
}
