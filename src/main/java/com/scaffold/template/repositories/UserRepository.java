package com.scaffold.template.repositories;

import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);
    UserEntity findByEmail(String email);

    @Query("SELECT DISTINCT t FROM UserEntity t " +
            "JOIN UserInterestEntity ui ON t.id = ui.user.id " +
            "JOIN UserInterestEntity ui2 ON ui.interest.id = ui2.interest.id " +
            "LEFT JOIN UserViewedProfileEntity v ON (v.viewer.id = :userId AND v.viewedUser.id = t.id) " +
            "WHERE t.role = 'TUTOR' " +
            "AND t.isActive = true " +
            "AND t.isVisible = true " +
            "AND ui2.user.id = :userId " +
            "AND (v IS NULL OR (v.status <> 'REJECTED' AND v.status <> 'ACCEPTED'))")
    List<UserEntity> findTutorsWithCommonInterests(@Param("userId") Long userId);

    List<UserEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<UserEntity> findByRole(Role role);
    List<UserEntity> findByCreatedAtBetweenAndRole(LocalDateTime start, LocalDateTime end, Role role);

    List<UserEntity> findByCreatedAtBefore(LocalDateTime localDateTime);

    long countByCreatedAtBefore(LocalDateTime localDateTime);
    long countByRoleAndCreatedAtBefore(Role role, LocalDateTime localDateTime);

    // Método para contar usuarios que no sean ADMIN y que hayan sido creados antes de una fecha específica
    long countByRoleNotAndCreatedAtBefore(Role role, LocalDateTime localDateTime);

    // Método para obtener Students y Tutors por fecha de creación
    @Query("SELECT u FROM UserEntity u WHERE u.role IN ('STUDENT', 'TUTOR') AND u.createdAt BETWEEN :startDate AND :endDate")
    List<UserEntity> findStudentsAndTutorsByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
