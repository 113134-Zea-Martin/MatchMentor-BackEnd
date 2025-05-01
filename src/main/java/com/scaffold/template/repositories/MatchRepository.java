package com.scaffold.template.repositories;

import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    List<MatchEntity> findByStatusAndTutorId(Status status, Long tutorId);
//    List<MatchEntity> findByStatusAndTutorId(Long status, Long studentId);
}
