package com.scaffold.template.repositories;

import com.scaffold.template.entities.MeetingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<MeetingEntity, Long> {
    boolean existsByMentorIdAndDateAndTimeAndStatus(Long mentorId, LocalDate date, LocalTime time, MeetingEntity.MeetingStatus status);
    boolean existsByStudentIdAndDateAndTimeAndStatus(Long studentId, LocalDate date, LocalTime time, MeetingEntity.MeetingStatus status);
    List<MeetingEntity> findByMentorIdAndDateAndStatusIn(Long mentorId, LocalDate date, List<MeetingEntity.MeetingStatus> statuses);
    List<MeetingEntity> findByStudentIdAndDateAndStatusIn(Long studentId, LocalDate date, List<MeetingEntity.MeetingStatus> statuses);
    List<MeetingEntity> findByStudentIdOrMentorId(Long studentId, Long mentorId);
    List<MeetingEntity> findByDate(LocalDate date);
    List<MeetingEntity> findByDateAndStatus(LocalDate date, MeetingEntity.MeetingStatus status);
}