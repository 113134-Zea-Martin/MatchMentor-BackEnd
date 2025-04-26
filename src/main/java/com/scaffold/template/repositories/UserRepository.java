package com.scaffold.template.repositories;

import com.scaffold.template.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
