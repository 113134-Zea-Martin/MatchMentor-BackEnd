package com.scaffold.template.services.meeting;

import com.scaffold.template.dtos.meeting.CreateMeetingRequestDTO;
import com.scaffold.template.dtos.meeting.MeetingHistoryResponseDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.MatchRepository;
import com.scaffold.template.repositories.MeetingRepository;
import com.scaffold.template.services.InterestService;
import com.scaffold.template.services.UserService;
import com.scaffold.template.services.chat.ChatService;
import com.scaffold.template.services.email.EmailService;
import com.scaffold.template.services.match.MatchService;
import com.scaffold.template.services.match.MatchServiceImpl;
import com.scaffold.template.services.notification.NotificationService;
import com.scaffold.template.services.userViewedProfileService.UserViewedProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MeetingServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private MatchService matchService;
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private EmailService emailService;


    @InjectMocks
    private MeetingServiceImpl meetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMeetingSuccessfullyWhenNoOverlap() {
        // Preparar datos
        Long studentId = 1L;
        Long mentorId = 2L;
        Long matchId = 3L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 1;
        String reason = "Tutoría de Java";

        UserEntity student = new UserEntity();
        student.setId(studentId);
        student.setEmail("student@example.com");

        UserEntity mentor = new UserEntity();
        mentor.setId(mentorId);
        mentor.setFirstName("Juan");
        mentor.setHourlyRate(25.0);

        MatchEntity match = new MatchEntity();
        match.setId(matchId);

        when(userService.getUserEntityById(studentId)).thenReturn(student);
        when(userService.getUserEntityById(mentorId)).thenReturn(mentor);
        when(matchService.getMatchEntityById(matchId)).thenReturn(match);
        when(meetingRepository.findByMentorIdAndDateAndStatusIn(eq(mentorId), eq(date), any()))
                .thenReturn(List.of());
        when(meetingRepository.findByStudentIdAndDateAndStatusIn(eq(studentId), eq(date), any()))
                .thenReturn(List.of());
        when(meetingRepository.existsByMentorIdAndDateAndTimeAndStatus(eq(mentorId), eq(date), eq(time), any()))
                .thenReturn(false);
        when(meetingRepository.existsByStudentIdAndDateAndTimeAndStatus(eq(studentId), eq(date), eq(time), any()))
                .thenReturn(false);
        when(meetingRepository.save(any())).thenAnswer(invocation -> {
            MeetingEntity savedMeeting = invocation.getArgument(0);
            savedMeeting.setId(1L);
            return savedMeeting;
        });

        // Ejecutar
        CreateMeetingRequestDTO result = meetingService.createMeeting(studentId, mentorId, matchId, date, time, duration, reason);

        // Verificar
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());
        assertEquals(mentorId, result.getMentorId());
        assertEquals(matchId, result.getMatchId());
        assertEquals(date, result.getDate());
        assertEquals(time, result.getTime());
        assertEquals(duration, result.getDuration());
        assertEquals(reason, result.getReason());

        verify(notificationService).createNotificationMeetingRequest(eq(studentId), eq(mentor.getFirstName()), any());
        verify(emailService).sendMeetingCreatedEmail(eq(student.getEmail()), eq(mentor.getFirstName()), any(), any());
    }

    @Test
    void createMeetingThrowsExceptionWhenMeetingsOverlap() {
        // Preparar
        Long studentId = 1L;
        Long mentorId = 2L;
        Long matchId = 3L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 1;
        String reason = "Tutoría de Java";

        // Crear una reunión existente que se solapa
        MeetingEntity existingMeeting = new MeetingEntity();
        existingMeeting.setDate(date);
        existingMeeting.setTime(time);
        existingMeeting.setDuration(1);

        when(meetingRepository.findByMentorIdAndDateAndStatusIn(eq(mentorId), eq(date), any()))
                .thenReturn(List.of(existingMeeting));

        // Ejecutar y verificar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                meetingService.createMeeting(studentId, mentorId, matchId, date, time, duration, reason));

        assertEquals("La reunión solicitada se solapa con una reunión existente del tutor.", exception.getMessage());
    }

    @Test
    void createMeetingThrowsExceptionWhenStudentMeetingsOverlap() {
        // Preparar
        Long studentId = 1L;
        Long mentorId = 2L;
        Long matchId = 3L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 1;
        String reason = "Tutoría de Java";

        when(meetingRepository.findByMentorIdAndDateAndStatusIn(eq(mentorId), eq(date), any()))
                .thenReturn(List.of());

        // Crear una reunión existente que se solapa
        MeetingEntity existingMeeting = new MeetingEntity();
        existingMeeting.setDate(date);
        existingMeeting.setTime(time);
        existingMeeting.setDuration(1);

        when(meetingRepository.findByStudentIdAndDateAndStatusIn(eq(studentId), eq(date), any()))
                .thenReturn(List.of(existingMeeting));

        // Ejecutar y verificar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                meetingService.createMeeting(studentId, mentorId, matchId, date, time, duration, reason));

        assertEquals("La reunión solicitada se solapa con una reunión existente del estudiante.", exception.getMessage());
    }

    @Test
    void createMeetingThrowsExceptionWhenMentorHasProposedMeeting() {
        // Preparar
        Long studentId = 1L;
        Long mentorId = 2L;
        Long matchId = 3L;
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 0);
        int duration = 1;
        String reason = "Tutoría de Java";

        when(meetingRepository.findByMentorIdAndDateAndStatusIn(eq(mentorId), eq(date), any()))
                .thenReturn(List.of());
        when(meetingRepository.findByStudentIdAndDateAndStatusIn(eq(studentId), eq(date), any()))
                .thenReturn(List.of());
        when(meetingRepository.existsByMentorIdAndDateAndTimeAndStatus(eq(mentorId), eq(date), eq(time), any()))
                .thenReturn(true);

        // Ejecutar y verificar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                meetingService.createMeeting(studentId, mentorId, matchId, date, time, duration, reason));

        assertEquals("Ya existe una reunión propuesta o aceptada para el mentor en la fecha y hora especificadas", exception.getMessage());
    }

    @Test
    void getMeetingHistoryReturnsFormattedResults() {
        // Preparar
        Long userId = 1L;

        UserEntity student = new UserEntity();
        student.setId(2L);
        student.setFirstName("Ana");
        student.setLastName("López");

        UserEntity mentor = new UserEntity();
        mentor.setId(userId);
        mentor.setFirstName("Juan");
        mentor.setLastName("García");

        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(1L);
        meeting.setStudent(student);
        meeting.setMentor(mentor);
        meeting.setDate(LocalDate.now());
        meeting.setTime(LocalTime.of(14, 0));
        meeting.setDuration(2);
        meeting.setReason("Revisión de proyecto");
        meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
        meeting.setHourlyRate(30.0);

        when(meetingRepository.findByStudentIdOrMentorId(userId, userId)).thenReturn(List.of(meeting));

        // Ejecutar
        List<MeetingHistoryResponseDTO> result = meetingService.getMeetingHistoryResponseDTO(userId);

        // Verificar
        assertEquals(1, result.size());
        MeetingHistoryResponseDTO dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("Ana López", dto.getOtherParticipantName());
        assertEquals("Mentor", dto.getMyRole());
        assertEquals(60.0, dto.getCost()); // 30 * 2
    }

    @Test
    void getMeetingHistoryThrowsExceptionWhenNoMeetingsFound() {
        // Preparar
        Long userId = 1L;
        when(meetingRepository.findByStudentIdOrMentorId(userId, userId)).thenReturn(List.of());

        // Ejecutar y verificar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                meetingService.getMeetingHistoryResponseDTO(userId));

        assertEquals("No se encontraron reuniones para el usuario con ID: " + userId, exception.getMessage());
    }

    @Test
    void respondToMeetingUpdatesStatusToAccepted() {
        // Preparar
        Long meetingId = 1L;
        boolean isAccepted = true;

        UserEntity student = new UserEntity();
        student.setFirstName("Ana");
        student.setLastName("López");

        UserEntity mentor = new UserEntity();
        mentor.setId(2L);
        mentor.setEmail("mentor@example.com");

        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(meetingId);
        meeting.setStudent(student);
        meeting.setMentor(mentor);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        // Ejecutar
        meetingService.respondToMeeting(meetingId, isAccepted);

        // Verificar
        assertEquals(MeetingEntity.MeetingStatus.ACCEPTED, meeting.getStatus());
        verify(notificationService).createNotificationMeetingAnswered(eq(meetingId), eq(mentor.getId()), eq("Ana López"), eq(isAccepted));
        verify(emailService).sendMeetingResponseEmail(eq(mentor.getEmail()), eq("Ana López"), eq(isAccepted));
        verify(meetingRepository).save(meeting);
    }

    @Test
    void respondToMeetingUpdatesStatusToRejected() {
        // Preparar
        Long meetingId = 1L;
        boolean isAccepted = false;

        UserEntity student = new UserEntity();
        student.setFirstName("Ana");
        student.setLastName("López");

        UserEntity mentor = new UserEntity();
        mentor.setId(2L);
        mentor.setEmail("mentor@example.com");

        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(meetingId);
        meeting.setStudent(student);
        meeting.setMentor(mentor);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        // Ejecutar
        meetingService.respondToMeeting(meetingId, isAccepted);

        // Verificar
        assertEquals(MeetingEntity.MeetingStatus.REJECTED, meeting.getStatus());
        verify(meetingRepository).save(meeting);
    }

    @Test
    void getMeetingByIdReturnsCorrectMeeting() {
        // Preparar
        Long meetingId = 1L;
        MeetingEntity meeting = new MeetingEntity();
        meeting.setId(meetingId);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

        // Ejecutar
        MeetingEntity result = meetingService.getMeetingById(meetingId);

        // Verificar
        assertEquals(meetingId, result.getId());
    }

    @Test
    void getMeetingByIdThrowsExceptionWhenMeetingNotFound() {
        // Preparar
        Long meetingId = 1L;
        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        // Ejecutar y verificar
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                meetingService.getMeetingById(meetingId));

        assertEquals("Reunión no encontrada con ID: " + meetingId, exception.getMessage());
    }
}