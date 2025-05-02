package com.scaffold.template.repositories;

import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findByStatusAndTutorId(Status status, Long tutorId);

    @Query("SELECT m FROM MatchEntity m WHERE m.status = :status AND (m.tutor.id = :userId OR m.student.id = :userId)")
    List<MatchEntity> findByStatusAndUserId(Status status, Long userId);
}
