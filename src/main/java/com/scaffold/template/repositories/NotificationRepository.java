package com.scaffold.template.repositories;

import com.scaffold.template.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // Obtener las notificaciones por usuario ordenadas por fecha de creación (las más recientes primero)
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Devuelve true si el usuario tiene notificaciones no leídas
    boolean existsByUserIdAndIsReadFalse(Long userId);

    // Devuelve las ultimas 10 notificaciones ordenadas por fecha de creación (las más recientes primero)
    List<NotificationEntity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);

}
