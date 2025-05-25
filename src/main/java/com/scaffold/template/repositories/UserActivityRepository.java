package com.scaffold.template.repositories;

import com.scaffold.template.entities.UserActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivityEntity, Long> {
    List<UserActivityEntity> findAllByActivityDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Boolean existsByUserIdAndActivityDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
