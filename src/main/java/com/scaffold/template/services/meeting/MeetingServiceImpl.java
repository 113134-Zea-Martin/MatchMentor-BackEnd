package com.scaffold.template.services.meeting;

import com.scaffold.template.dtos.meeting.CreateMeetingRequestDTO;
import com.scaffold.template.dtos.meeting.MeetingHistoryResponseDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.MeetingRepository;
import com.scaffold.template.services.UserService;
import com.scaffold.template.services.email.EmailService;
import com.scaffold.template.services.match.MatchService;
import com.scaffold.template.services.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingServiceImpl implements MeetingService {

    private static final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);
    private final MeetingRepository meetingRepository;
    private final UserService userService;
    private final MatchService matchService;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Autowired
    public MeetingServiceImpl(MeetingRepository meetingRepository,
                              UserService userService,
                              MatchService matchService,
                              NotificationService notificationService,
                              EmailService emailService) {
        this.meetingRepository = meetingRepository;
        this.userService = userService;
        this.matchService = matchService;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    private LocalDateTime mapToLocalDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    private LocalDateTime timeAddInHours(LocalDateTime dateTime, int hours) {
        return dateTime.plusHours(hours);
    }

    private Boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    @Override
    public CreateMeetingRequestDTO createMeeting(Long studentId, Long mentorId, Long matchId, LocalDate date, LocalTime time, int duration, String reason) {

        List<MeetingEntity> meetings = meetingRepository.findByMentorIdAndDateAndStatusIn(mentorId, date, Arrays.asList(
                MeetingEntity.MeetingStatus.PROPOSED,
                MeetingEntity.MeetingStatus.ACCEPTED
        ));

        for(MeetingEntity meeting : meetings) {
            LocalDateTime start = mapToLocalDateTime(meeting.getDate(), meeting.getTime());
            LocalDateTime end = timeAddInHours(start, meeting.getDuration());
            LocalDateTime newStart = mapToLocalDateTime(date, time);
            LocalDateTime newEnd = timeAddInHours(newStart, duration);

            if (isOverlapping(start, end, newStart, newEnd)) {
                throw new IllegalArgumentException("La reunión solicitada se solapa con una reunión existente del tutor.");
            }
        }

        List<MeetingEntity> studentMeetings = meetingRepository.findByStudentIdAndDateAndStatusIn(studentId, date, Arrays.asList(
                MeetingEntity.MeetingStatus.PROPOSED,
                MeetingEntity.MeetingStatus.ACCEPTED
        ));

        for(MeetingEntity meeting : studentMeetings) {
            LocalDateTime start = mapToLocalDateTime(meeting.getDate(), meeting.getTime());
            LocalDateTime end = timeAddInHours(start, meeting.getDuration());
            LocalDateTime newStart = mapToLocalDateTime(date, time);
            LocalDateTime newEnd = timeAddInHours(newStart, duration);

            if (isOverlapping(start, end, newStart, newEnd)) {
                throw new IllegalArgumentException("La reunión solicitada se solapa con una reunión existente del estudiante.");
            }
        }

        // Check si el mentor ya tiene una reunión propuesta o aceptada en la fecha y hora especificadas
        if (meetingRepository.existsByMentorIdAndDateAndTimeAndStatus(mentorId, date, time, MeetingEntity.MeetingStatus.PROPOSED) ||
                meetingRepository.existsByMentorIdAndDateAndTimeAndStatus(mentorId, date, time, MeetingEntity.MeetingStatus.ACCEPTED)) {
            throw new IllegalArgumentException("Ya existe una reunión propuesta o aceptada para el mentor en la fecha y hora especificadas");
        }

        // Check si el estudiante ya tiene una reunión aceptada en la fecha y hora especificadas
        if (meetingRepository.existsByStudentIdAndDateAndTimeAndStatus(studentId, date, time, MeetingEntity.MeetingStatus.ACCEPTED)) {
            throw new IllegalArgumentException("Ya existe una reunión aceptada para el estudiante en la fecha y hora especificadas");
        }

        // Calcular la hora de fin para la nueva reunión
        LocalTime endTime = time.plusHours(duration);

        // Verificar si hay reuniones solapadas para el mentor
        List<MeetingEntity.MeetingStatus> activeStatuses = Arrays.asList(
                MeetingEntity.MeetingStatus.PROPOSED,
                MeetingEntity.MeetingStatus.ACCEPTED
        );

        List<String> statusesAsStrings = activeStatuses.stream()
                .map(Enum::name)
                .collect(Collectors.toList());


        // Puedes hacer la misma validación para el estudiante si es necesario

        // Validate the student and mentor IDs
        UserEntity student = userService.getUserEntityById(studentId);
        UserEntity mentor = userService.getUserEntityById(mentorId);
        MatchEntity match = matchService.getMatchEntityById(matchId);

        if (student == null || mentor == null || match == null) {
            throw new IllegalArgumentException("Estudiante, mentor o match no encontrado");
        }

        // Create the meeting using the repository
        MeetingEntity meeting = new MeetingEntity();
        meeting.setStudent(student);
        meeting.setMentor(mentor);
        meeting.setMatch(match);
        meeting.setDate(date);
        meeting.setTime(time);
        meeting.setDuration(duration);
        meeting.setReason(reason);

        meeting.setStatus(MeetingEntity.MeetingStatus.PROPOSED);
        meeting.setCreatedAt(LocalDateTime.now());

        // Save the meeting to the database
        meetingRepository.save(meeting);

        // Optionally, you can add logic to send notifications or perform other actions here
        // For example, you can create a notification for the student
        notificationService.createNotificationMeetingRequest(studentId, mentor.getFirstName(), matchId);

        // Formatear la fecha a "dd/MM/yyyy", por ejemplo "08/05/2023"
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Email the student about the meeting request
        emailService.sendMeetingCreatedEmail(student.getEmail(), mentor.getFirstName(), formattedDate, time.toString());

        // Return the created meeting

        return mapToDTO(meeting);
    }

    public CreateMeetingRequestDTO mapToDTO(MeetingEntity meeting) {
        CreateMeetingRequestDTO dto = new CreateMeetingRequestDTO();
        dto.setStudentId(meeting.getStudent().getId());
        dto.setMentorId(meeting.getMentor().getId());
        dto.setMatchId(meeting.getMatch().getId());
        dto.setDate(meeting.getDate());
        dto.setTime(meeting.getTime());
        dto.setDuration(meeting.getDuration());
        dto.setReason(meeting.getReason());
        return dto;
    }

    public List<MeetingEntity> getMeetingHistory(Long userId) {
        List<MeetingEntity> meetings = meetingRepository.findByStudentIdOrMentorId(userId, userId);
        if (meetings.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron reuniones para el usuario con ID: " + userId);
        }

        for (MeetingEntity meeting : meetings) {
            if (meeting.getStudent().getId().equals(userId)) {
                meeting.setStudent(null);
            } else {
                meeting.setMentor(null);
            }
        }

        return meetings;
    }

    public MeetingHistoryResponseDTO mapToMeetingHistoryResponseDTO(MeetingEntity meeting) {
        MeetingHistoryResponseDTO dto = new MeetingHistoryResponseDTO();
        dto.setDate(meeting.getDate());
        dto.setTime(meeting.getTime());
        dto.setDuration(meeting.getDuration());

        dto.setOtherParticipantName(meeting.getStudent() != null ?
                meeting.getStudent().getFirstName() + ' ' + meeting.getStudent().getLastName() :
                meeting.getMentor().getFirstName() + ' ' + meeting.getMentor().getLastName());

        dto.setMyRole(meeting.getStudent() == null ? "Estudiante" : "Mentor");

        dto.setReason(meeting.getReason());
        dto.setStatus(meeting.getStatus());
        return dto;
    }

    @Override
    public List<MeetingHistoryResponseDTO> getMeetingHistoryResponseDTO(Long userId) {
        List<MeetingEntity> meetings = getMeetingHistory(userId);
        return meetings.stream()
                .map(this::mapToMeetingHistoryResponseDTO)
                .collect(Collectors.toList());
    }
}
