package com.scaffold.template.repositories;

import com.scaffold.template.dtos.adminReport.ResponseReportDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

    List<MatchEntity> findByCreatedAtBetweenAndStatus(LocalDateTime from, LocalDateTime to, Status status);

    List<MatchEntity> findByStatusAndTutorId(Status status, Long tutorId);

    @Query("SELECT m FROM MatchEntity m WHERE m.status = :status AND (m.tutor.id = :userId OR m.student.id = :userId)")
    List<MatchEntity> findByStatusAndUserId(Status status, Long userId);

    Integer countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    Integer countByCreatedAtBetweenAndStatus(LocalDateTime from, LocalDateTime to, Status status);


//    SELECT COUNT(m.tutor_id) as quantity, CONCAT(u.first_name, " ", u.last_name) as full_name FROM matches m
//    JOIN users u ON m.tutor_id = u.id
//    WHERE m.updated_at BETWEEN '2025-05-14' AND '2025-05-28'
//    AND m.status = 2
//    GROUP BY m.tutor_id
//    ORDER BY quantity DESC
//    LIMIT 3;
//    @Query("SELECT (" +
//            "COUNT(m.tutor.id), CONCAT(t.firstName, ' ', t.lastName)) " +
//            "FROM MatchEntity m " +
//            "JOIN m.tutor t " +
//            "WHERE m.updatedAt BETWEEN :startDateTime AND :endDateTime " +
//            "AND m.status = 'ACCEPTED' " +
//            "GROUP BY m.tutor.id " +
//            "ORDER BY COUNT(m.tutor.id) DESC " +
//            "LIMIT 3")
//    List<Object[]> findTop3TutorsByMatchesAccepted(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = "SELECT COUNT(m.tutor_id) AS quantity, " +
            "CONCAT(u.first_name, ' ', u.last_name) AS fullName " +
            "FROM matches m " +
            "JOIN users u ON m.tutor_id = u.id " +
            "WHERE m.created_at BETWEEN :startDateTime AND :endDateTime " +
//            "AND m.status = 2 " +
            "GROUP BY m.tutor_id " +
            "ORDER BY quantity DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTutorsByAcceptedMatches(LocalDateTime startDateTime, LocalDateTime endDateTime);


    //    SELECT COUNT(c.interest_id) AS quantity, name  FROM match_common_interests c
//    JOIN matches m ON m.id = c.match_id
//    JOIN interests i ON i.id = c.interest_id
//    WHERE m.status = 2
//    AND m.updated_at BETWEEN '2025-05-19' AND '2025-05-30'
//    GROUP BY name
//    ORDER BY quantity DESC
//    LIMIT 5;
    @Query(value = "SELECT COUNT(c.interest_id) AS quantity, i.name AS name " +
            "FROM match_common_interests c " +
            "JOIN matches m ON m.id = c.match_id " +
            "JOIN interests i ON i.id = c.interest_id " +
            "WHERE m.created_at BETWEEN :startDateTime AND :endDateTime " +
//            "AND m.status = 2" +
            "GROUP BY i.name " +
            "ORDER BY quantity DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> findTopInterestByMatchAccepted(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
