package com.scaffold.template.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity representing a meeting between a student and a mentor.
 * Maps to the "meetings" table in the database.
 */
@Data
@Entity
@Table(name = "meetings")
public class MeetingEntity {

    /**
     * Unique identifier for the meeting.
     * Maps to the "id" column (BIGINT PRIMARY KEY AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The student participating in the meeting.
     * Foreign key referencing the "id" column in the "users" table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    /**
     * The mentor participating in the meeting.
     * Foreign key referencing the "id" column in the "users" table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private UserEntity mentor;

    /**
     * The match associated with the meeting.
     * Foreign key referencing the "id" column in the "matches" table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    /**
     * The date of the meeting.
     * Maps to the "fecha" column (DATE NOT NULL).
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * The time of the meeting.
     * Maps to the "hora" column (TIME NOT NULL).
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;

    /**
     * The duration of the meeting in hours.
     * Maps to the "duration" column (INTEGER).
     */
    private int duration;

    /**
     * The reason or purpose of the meeting.
     * Maps to the "motivo" column (TEXT).
     */
    private String reason;

    /**
     * The status of the meeting.
     * Maps to the "estado" column (ENUM: PROPUESTA, ACEPTADA, RECHAZADA).
     */
    @Enumerated(EnumType.STRING)
    private MeetingStatus status;

    /**
     * The timestamp when the meeting was created.
     * Maps to the "fechaCreacion" column (TIMESTAMP DEFAULT CURRENT_TIMESTAMP).
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * The timestamp when the meeting was last updated.
     * Maps to the "fechaActualizacion" column (TIMESTAMP ON UPDATE CURRENT_TIMESTAMP).
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private double hourlyRate;

    /**
     * Enum representing the possible statuses of a meeting.
     */
    public enum MeetingStatus {
        PROPOSED,  // The meeting has been proposed but not yet accepted or rejected.
        ACCEPTED,  // The meeting has been accepted by both parties.
        REJECTED   // The meeting has been rejected.
    }
}