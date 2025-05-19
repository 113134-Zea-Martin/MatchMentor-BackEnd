package com.scaffold.template.services.match;

import com.scaffold.template.dtos.match.ConfirmedMatchResponseDTO;
import com.scaffold.template.dtos.match.MatchResponseDTO;
import com.scaffold.template.dtos.match.StudentPendingRequestMatchDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.InterestEntity;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.Status;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserInterestEntity;
import com.scaffold.template.repositories.MatchRepository;
import com.scaffold.template.repositories.MeetingRepository;
import com.scaffold.template.services.InterestService;
import com.scaffold.template.services.UserService;
import com.scaffold.template.services.chat.ChatService;
import com.scaffold.template.services.email.EmailService;
import com.scaffold.template.services.email.RecordatorioMeetingService;
import com.scaffold.template.services.notification.NotificationService;
import com.scaffold.template.services.userViewedProfileService.UserViewedProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MatchServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private EmailService emailService;
    @Mock
    private ChatService chatService;
    @Mock
    private InterestService interestService;
    @Mock
    private UserViewedProfileService userViewedProfileService;


    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getIDsOfTutorsWithCommonInterestsReturnsCorrectIDs() {
        Long studentId = 1L;

        UserEntity tutor1 = new UserEntity();
        tutor1.setId(2L);
        tutor1.setRole(Role.TUTOR);

        UserEntity tutor2 = new UserEntity();
        tutor2.setId(3L);
        tutor2.setRole(Role.TUTOR);

        List<UserEntity> tutors = List.of(tutor1, tutor2);

        when(userService.findTutorsWithCommonInterests(studentId)).thenReturn(tutors);

        List<Long> tutorIds = matchService.getIDsOfTutorsWithCommonInterests(studentId);

        assertEquals(2, tutorIds.size());
        assertEquals(2L, tutorIds.get(0));
        assertEquals(3L, tutorIds.get(1));
        verify(userService).findTutorsWithCommonInterests(studentId);
    }

    @Test
    void getTutorCompatibleWithStudentReturnsFirstCompatibleTutor() {
        Long studentId = 1L;

        UserEntity tutor1 = new UserEntity();
        tutor1.setId(2L);

        UserResponseDTO tutorResponseDTO = new UserResponseDTO();
        tutorResponseDTO.setId(2L);
        tutorResponseDTO.setFirstName("Juan");
        tutorResponseDTO.setLastName("Pérez");

        when(userService.findTutorsWithCommonInterests(studentId)).thenReturn(List.of(tutor1));
        when(userService.getUserById(2L)).thenReturn(tutorResponseDTO);

        UserResponseDTO result = matchService.getTutorCompatibleWithStudent(studentId);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Juan", result.getFirstName());
    }

    @Test
    void getTutorCompatibleWithStudentReturnsNullWhenNoCompatibleTutors() {
        Long studentId = 1L;

        when(userService.findTutorsWithCommonInterests(studentId)).thenReturn(List.of());

        UserResponseDTO result = matchService.getTutorCompatibleWithStudent(studentId);

        assertNull(result);
    }

    @Test
    void createMatchCreatesMatchWithPendingStatusWhenAccepted() {
        Long studentId = 1L;
        Long tutorId = 2L;

        UserEntity student = new UserEntity();
        student.setId(studentId);
        student.setFirstName("Carlos");
        student.setEmail("carlos@example.com");

        UserEntity tutor = new UserEntity();
        tutor.setId(tutorId);
        tutor.setFirstName("Laura");
        tutor.setEmail("laura@example.com");

        List<UserInterestEntity> studentInterests = new ArrayList<>();
        UserInterestEntity interest1 = new UserInterestEntity();
        InterestEntity interestEntity1 = new InterestEntity();
        interestEntity1.setId(1L);
        interest1.setInterest(interestEntity1);
        studentInterests.add(interest1);
        student.setUserInterests(studentInterests);

        List<UserInterestEntity> tutorInterests = new ArrayList<>();
        UserInterestEntity interest2 = new UserInterestEntity();
        InterestEntity interestEntity2 = new InterestEntity();
        interestEntity2.setId(1L);
        interest2.setInterest(interestEntity2);
        tutorInterests.add(interest2);
        tutor.setUserInterests(tutorInterests);

        when(userService.getUserEntityById(studentId)).thenReturn(student);
        when(userService.getUserEntityById(tutorId)).thenReturn(tutor);

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(1L);
        matchEntity.setStudent(student);
        matchEntity.setTutor(tutor);
        matchEntity.setStatus(Status.PENDING);

        when(matchRepository.save(any(MatchEntity.class))).thenReturn(matchEntity);

        MatchEntity result = matchService.createMatch(studentId, tutorId, true);

        assertNotNull(result);
        assertEquals(Status.PENDING, result.getStatus());
        verify(notificationService).createNotificationConnectionRequest(eq(tutorId), eq("Carlos"), eq(1L));
        verify(emailService).sendMatchRequestEmail("laura@example.com", "Carlos");
    }

    @Test
    void createMatchCreatesMatchWithRejectedStatusWhenNotAccepted() {
        Long studentId = 1L;
        Long tutorId = 2L;

        UserEntity student = new UserEntity();
        student.setId(studentId);
        student.setFirstName("Carlos");

        UserEntity tutor = new UserEntity();
        tutor.setId(tutorId);

        when(userService.getUserEntityById(studentId)).thenReturn(student);
        when(userService.getUserEntityById(tutorId)).thenReturn(tutor);

        student.setUserInterests(new ArrayList<>());
        tutor.setUserInterests(new ArrayList<>());

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(1L);
        matchEntity.setStatus(Status.REJECTED);

        when(matchRepository.save(any(MatchEntity.class))).thenReturn(matchEntity);

        MatchEntity result = matchService.createMatch(studentId, tutorId, false);

        assertNotNull(result);
        assertEquals(Status.REJECTED, result.getStatus());
        verify(notificationService, never()).createNotificationConnectionRequest(anyLong(), anyString(), anyLong());
        verify(emailService, never()).sendMatchRequestEmail(anyString(), anyString());
    }

    @Test
    void mapToMatchResponseDTOCorrectlyMapsDTOFields() {
        UserEntity student = new UserEntity();
        student.setId(1L);

        UserEntity tutor = new UserEntity();
        tutor.setId(2L);

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(5L);
        matchEntity.setStudent(student);
        matchEntity.setTutor(tutor);
        matchEntity.setStatus(Status.PENDING);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        matchEntity.setCreatedAt(createdAt);
        matchEntity.setUpdatedAt(updatedAt);

        MatchResponseDTO result = matchService.mapToMatchResponseDTO(matchEntity);

        assertEquals(5L, result.getId());
        assertEquals(1L, result.getStudentId());
        assertEquals(2L, result.getTutorId());
        assertEquals(Status.PENDING, result.getStatus());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());
    }

    @Test
    void acceptMatchUpdatesStatusToAcceptedAndSendsNotification() {
        Long matchId = 1L;

        UserEntity student = new UserEntity();
        student.setId(1L);
        student.setFirstName("Carlos");
        student.setEmail("carlos@example.com");

        UserEntity tutor = new UserEntity();
        tutor.setId(2L);
        tutor.setFirstName("Laura");

        MatchEntity match = new MatchEntity();
        match.setId(matchId);
        match.setStudent(student);
        match.setTutor(tutor);
        match.setStatus(Status.PENDING);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(match);

        MatchEntity result = matchService.acceptMatch(matchId, true);

        assertNotNull(result);
        assertEquals(Status.ACCEPTED, result.getStatus());
        verify(emailService).sendMatchAcceptedEmail("carlos@example.com", "Laura");
        verify(notificationService).createNotificationConnectionAnswered(eq(1L), eq("Laura"), eq(matchId), eq(true));
    }

    @Test
    void acceptMatchUpdatesStatusToRejectedAndSendsNotification() {
        Long matchId = 1L;

        UserEntity student = new UserEntity();
        student.setId(1L);

        UserEntity tutor = new UserEntity();
        tutor.setId(2L);
        tutor.setFirstName("Laura");

        MatchEntity match = new MatchEntity();
        match.setId(matchId);
        match.setStudent(student);
        match.setTutor(tutor);
        match.setStatus(Status.PENDING);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(match);

        MatchEntity result = matchService.acceptMatch(matchId, false);

        assertNotNull(result);
        assertEquals(Status.REJECTED, result.getStatus());
        verify(emailService, never()).sendMatchAcceptedEmail(anyString(), anyString());
        verify(notificationService).createNotificationConnectionAnswered(eq(1L), eq("Laura"), eq(matchId), eq(false));
    }

    @Test
    void getMatchEntityByIdReturnsMatchWhenExists() {
        Long matchId = 1L;

        MatchEntity match = new MatchEntity();
        match.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        MatchEntity result = matchService.getMatchEntityById(matchId);

        assertNotNull(result);
        assertEquals(matchId, result.getId());
    }

    @Test
    void getMatchEntityByIdThrowsExceptionWhenMatchNotFound() {
        Long matchId = 99L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.getMatchEntityById(matchId));
    }

    @Test
    void mapToConfirmedMatchResponseDTOCorrectlyMapsStudentPerspective() {
        Long userId = 1L;
        Long matchId = 5L;

        UserEntity student = new UserEntity();
        student.setId(1L);

        UserEntity tutor = new UserEntity();
        tutor.setId(2L);
        tutor.setFirstName("Laura");
        tutor.setLastName("Martínez");
        tutor.setCurrentProfession("Desarrolladora Senior");
        tutor.setIsActive(true);

        MatchEntity match = new MatchEntity();
        match.setId(matchId);
        match.setStudent(student);
        match.setTutor(tutor);
        match.setUpdatedAt(LocalDateTime.now());
        match.setCreatedAt(LocalDateTime.now().minusDays(1));
        match.setCommonInterests(List.of(1L, 2L));

        when(userService.getUserEntityById(2L)).thenReturn(tutor);
        when(chatService.existsByMatchIdAndSenderIdAndIsReadFalse(matchId, 2L)).thenReturn(true);

        InterestEntity interest1 = new InterestEntity();
        interest1.setId(1L);
        interest1.setName("Java");

        InterestEntity interest2 = new InterestEntity();
        interest2.setId(2L);
        interest2.setName("Python");

        when(interestService.getById(1L)).thenReturn(interest1);
        when(interestService.getById(2L)).thenReturn(interest2);

        when(interestService.getById(1L)).thenReturn(interest1);
        when(interestService.getById(2L)).thenReturn(interest2);

        ConfirmedMatchResponseDTO result = matchService.mapToConfirmedMatchResponseDTO(match, userId);

        assertNotNull(result);
        assertEquals(matchId, result.getId());
        assertEquals(2L, result.getUserId());
        assertEquals("Laura Martínez", result.getFullName());
        assertEquals(Role.TUTOR, result.getRole());
        assertEquals("Desarrolladora Senior", result.getDescription());
        assertTrue(result.getIsActive());
        assertTrue(result.getHasUnreadMessages());
        assertEquals(2, result.getCommonInterests().size());
        assertTrue(result.getCommonInterests().contains("Java"));
        assertTrue(result.getCommonInterests().contains("Python"));
    }

    @Test
    void mapToStudentPendingRequestMatchDTOsReturnsCorrectlyMappedDTOs() {
        UserEntity student = new UserEntity();
        student.setId(1L);
        student.setFirstName("Ana");
        student.setLastName("López");
        student.setStudyArea("Informática");
        student.setEducationLevel("Universitario");
        student.setMentoringGoals("Aprender programación");

        List<UserInterestEntity> interests = new ArrayList<>();
        UserInterestEntity interest = new UserInterestEntity();
        InterestEntity interestEntity = new InterestEntity();
        interestEntity.setId(1L);
        interestEntity.setName("Java");
        interest.setInterest(interestEntity);
        interests.add(interest);
        student.setUserInterests(interests);

        UserEntity tutor = new UserEntity();
        tutor.setId(2L);

        MatchEntity match = new MatchEntity();
        match.setId(5L);
        match.setStudent(student);
        match.setTutor(tutor);
        match.setStatus(Status.PENDING);

        List<MatchEntity> matches = List.of(match);

        List<StudentPendingRequestMatchDTO> result = matchService.mapToStudentPendingRequestMatchDTOs(matches);

        assertEquals(1, result.size());
        StudentPendingRequestMatchDTO dto = result.get(0);
        assertEquals(5L, dto.getId());
        assertEquals(1L, dto.getStudentId());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("López", dto.getLastName());
        assertEquals("Informática", dto.getStudyArea());
        assertEquals("Universitario", dto.getEducationLevel());
        assertEquals("Aprender programación", dto.getMentoringGoals());
        assertEquals(1, dto.getStudentInterests().size());
        assertEquals("Java", dto.getStudentInterests().get(0));
    }

    @Test
    void mapToStudentPendingRequestMatchDTOsHandlesMultipleMatches() {
        UserEntity student1 = new UserEntity();
        student1.setId(1L);
        student1.setFirstName("Ana");
        student1.setLastName("López");
        student1.setUserInterests(new ArrayList<>());

        UserEntity student2 = new UserEntity();
        student2.setId(2L);
        student2.setFirstName("Carlos");
        student2.setLastName("Rodríguez");
        student2.setUserInterests(new ArrayList<>());

        UserEntity tutor = new UserEntity();
        tutor.setId(3L);

        MatchEntity match1 = new MatchEntity();
        match1.setId(5L);
        match1.setStudent(student1);
        match1.setTutor(tutor);

        MatchEntity match2 = new MatchEntity();
        match2.setId(6L);
        match2.setStudent(student2);
        match2.setTutor(tutor);

        List<MatchEntity> matches = List.of(match1, match2);

        List<StudentPendingRequestMatchDTO> result = matchService.mapToStudentPendingRequestMatchDTOs(matches);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getStudentId());
        assertEquals("Ana", result.get(0).getFirstName());
        assertEquals(2L, result.get(1).getStudentId());
        assertEquals("Carlos", result.get(1).getFirstName());
    }

    @Test
    void mapToStudentPendingRequestMatchDTOsHandlesEmptyMatchesList() {
        List<MatchEntity> matches = List.of();

        List<StudentPendingRequestMatchDTO> result = matchService.mapToStudentPendingRequestMatchDTOs(matches);

        assertTrue(result.isEmpty());
    }

//    @Test
//    void mapToStudentPendingRequestMatchDTOsHandlesNullInterests() {
//        UserEntity student = new UserEntity();
//        student.setId(1L);
//        student.setFirstName("Ana");
//        student.setLastName("López");
//        student.setUserInterests(null);
//
//        UserEntity tutor = new UserEntity();
//        tutor.setId(2L);
//
//        MatchEntity match = new MatchEntity();
//        match.setId(5L);
//        match.setStudent(student);
//        match.setTutor(tutor);
//
//        List<MatchEntity> matches = List.of(match);
//
//        List<StudentPendingRequestMatchDTO> result = matchService.mapToStudentPendingRequestMatchDTOs(matches);
//
//        assertEquals(1, result.size());
//        assertEquals(0, result.get(0).getStudentInterests().size());
//    }

    @Test
    void mapToStudentPendingRequestMatchDTOsHandlesMultipleInterests() {
        UserEntity student = new UserEntity();
        student.setId(1L);
        student.setFirstName("Ana");

        List<UserInterestEntity> interests = new ArrayList<>();

        UserInterestEntity interest1 = new UserInterestEntity();
        InterestEntity interestEntity1 = new InterestEntity();
        interestEntity1.setId(1L);
        interestEntity1.setName("Java");
        interest1.setInterest(interestEntity1);
        interests.add(interest1);

        UserInterestEntity interest2 = new UserInterestEntity();
        InterestEntity interestEntity2 = new InterestEntity();
        interestEntity2.setId(2L);
        interestEntity2.setName("Python");
        interest2.setInterest(interestEntity2);
        interests.add(interest2);

        student.setUserInterests(interests);

        UserEntity tutor = new UserEntity();
        tutor.setId(2L);

        MatchEntity match = new MatchEntity();
        match.setId(5L);
        match.setStudent(student);
        match.setTutor(tutor);

        List<MatchEntity> matches = List.of(match);

        List<StudentPendingRequestMatchDTO> result = matchService.mapToStudentPendingRequestMatchDTOs(matches);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getStudentInterests().size());
        assertTrue(result.get(0).getStudentInterests().contains("Java"));
        assertTrue(result.get(0).getStudentInterests().contains("Python"));
    }
}