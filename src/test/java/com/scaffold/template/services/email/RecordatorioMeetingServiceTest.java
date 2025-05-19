package com.scaffold.template.services.email;

import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.MeetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecordatorioMeetingServiceTest {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private RecordatorioMeetingService recordatorioMeetingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSendEmailSuccessfully() {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setReason("meeting");
        LocalDate today = LocalDate.now();
        meeting.setDate(today);

        UserEntity mentor = new UserEntity();
        mentor.setFirstName("Mentor");
        mentor.setLastName("MentorLastName");
        meeting.setMentor(mentor);

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

//        SimpleMailMessage expectedMessage = new SimpleMailMessage();
//        expectedMessage.setTo("user@example.com");
//        expectedMessage.setSubject("Recordatorio: Team Meeting");
//        expectedMessage.setText("La reunión 'Team Meeting' está programada para el 2023-10-10.");

        // En lugar de crear un mensaje específico, usamos ArgumentCaptor para capturar el mensaje real enviado
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

//        doNothing().when(mailSender).send(expectedMessage);

//        recordatorioMeetingService.sendMeetingReminder(meeting, user);
        recordatorioMeetingService.sendMail(user.getEmail(), meeting,
                meeting.getMentor().getFirstName() + " " + meeting.getMentor().getLastName());

//        verify(mailSender, times(1)).send(expectedMessage);
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals("user@example.com", sentMessage.getTo()[0]);
        assertEquals("Recordatorio de reunión programada", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Mentor MentorLastName"));
        assertTrue(sentMessage.getText().contains("meeting"));
    }

    @Test
    void shouldNotSendEmailWhenUserEmailIsNull() {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setReason("meeting");
        LocalDate today = LocalDate.now();
        meeting.setDate(today);

        UserEntity mentor = new UserEntity();
        mentor.setFirstName("Mentor");
        mentor.setLastName("MentorLastName");
        meeting.setMentor(mentor);

//        UserEntity user = new UserEntity();
//        user.setEmail(null);
        // Debemos verificar que el servicio no envíe correos cuando el email es null
        // Podríamos necesitar modificar la implementación del servicio para esta prueba
//        doCallRealMethod().when(recordatorioMeetingService).sendMail(isNull(), any(MeetingEntity.class), anyString());
        recordatorioMeetingService.sendMail(null, meeting,
                meeting.getMentor().getFirstName() + " " + meeting.getMentor().getLastName());

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldNotSendEmailWhenMeetingTitleIsNull() {
        MeetingEntity meeting = new MeetingEntity();
        meeting.setReason(null); // Asumiendo que reason es el "título" que queremos probar
        LocalDate today = LocalDate.now();
        meeting.setDate(today);

        UserEntity mentor = new UserEntity();
        mentor.setFirstName("Mentor");
        mentor.setLastName("MentorLastName");
        meeting.setMentor(mentor);

        recordatorioMeetingService.sendMail("user@example.com", meeting,
                meeting.getMentor().getFirstName() + " " + meeting.getMentor().getLastName());

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }


//    @Test
//    void shouldSendReminderEmailsToMentorAndStudentWhenMeetingIsToday() {
//        LocalDate today = LocalDate.now();
//
//        UserEntity student = new UserEntity();
//        student.setFirstName("Student");
//        student.setLastName("LastName");
//        student.setEmail("student@example.com");
//
//        UserEntity mentor = new UserEntity();
//        mentor.setFirstName("Mentor");
//        mentor.setLastName("LastName");
//        mentor.setEmail("mentor@example.com");
//
//        MeetingEntity meeting = new MeetingEntity();
//        meeting.setDate(today);
//        meeting.setTime(LocalTime.now());
//        meeting.setReason("Tutoría semanal");
//        meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
//        meeting.setStudent(student);
//        meeting.setMentor(mentor);
//
//        List<MeetingEntity> todaysMeetings = List.of(meeting);
//        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
//                .thenReturn(todaysMeetings);
//
//        recordatorioMeetingService.sendRecordatory();
//
//        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//        verify(mailSender, times(2)).send(messageCaptor.capture());
//
//        List<SimpleMailMessage> capturedMessages = messageCaptor.getAllValues();
//
//        SimpleMailMessage studentMessage = capturedMessages.get(0);
//        assertEquals("student@example.com", studentMessage.getTo()[0]);
//        assertEquals("Recordatorio de reunión programada", studentMessage.getSubject());
//        assertTrue(studentMessage.getText().contains("Mentor LastName"));
//        assertTrue(studentMessage.getText().contains("Tutoría semanal"));
//
//
//        SimpleMailMessage mentorMessage = capturedMessages.get(1);
//        assertEquals("mentor@example.com", mentorMessage.getTo()[0]);
//        assertEquals("Recordatorio de reunión programada", mentorMessage.getSubject());
//        assertTrue(mentorMessage.getText().contains("Student LastName"));
//        assertTrue(mentorMessage.getText().contains("Tutoría semanal"));
//    }
//
//    @Test
//    void shouldNotSendReminderEmailsWhenNoMeetingsForToday() {
//        LocalDate today = LocalDate.now();
//        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
//                .thenReturn(List.of());
//
//        recordatorioMeetingService.sendRecordatory();
//
//        verify(mailSender, never()).send(any(SimpleMailMessage.class));
//    }
//
//    @Test
//    void shouldHandleNullStudentOrMentorEmailGracefully() {
//        LocalDate today = LocalDate.now();
//
//        UserEntity student = new UserEntity();
//        student.setEmail(null); // Estudiante sin email
//
//        UserEntity mentor = new UserEntity();
//        mentor.setFirstName("Mentor");
//        mentor.setLastName("LastName");
//        mentor.setEmail("mentor@example.com");
//
//        MeetingEntity meeting = new MeetingEntity();
//        meeting.setDate(today);
//        meeting.setReason("Tutoría semanal");
//        meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
//        meeting.setStudent(student);
//        meeting.setMentor(mentor);
//
//        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
//                .thenReturn(List.of(meeting));
//
//        recordatorioMeetingService.sendRecordatory();
//
//        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//        verify(mailSender, times(1)).send(messageCaptor.capture());
//
//        SimpleMailMessage mentorMessage = messageCaptor.getValue();
//        Assertions.assertNotNull(mentorMessage.getTo());
//        assertEquals("mentor@example.com", mentorMessage.getTo()[0]);
//    }

    @Test
    void sendRecordatorySendsTwoEmailsForEachMeeting() {
        LocalDate today = LocalDate.now();

        UserEntity student = new UserEntity();
        student.setFirstName("Student");
        student.setLastName("LastName");
        student.setEmail("student@example.com");

        UserEntity mentor = new UserEntity();
        mentor.setFirstName("Mentor");
        mentor.setLastName("LastName");
        mentor.setEmail("mentor@example.com");

        MeetingEntity meeting = new MeetingEntity();
        meeting.setDate(today);
        LocalTime now = LocalTime.now();
        meeting.setTime(now);
        meeting.setReason("Tutoría semanal");
        meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
        meeting.setStudent(student);
        meeting.setMentor(mentor);

        List<MeetingEntity> todaysMeetings = List.of(meeting);
        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
                .thenReturn(todaysMeetings);

        recordatorioMeetingService.sendRecordatory();

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(2)).send(messageCaptor.capture());

        List<SimpleMailMessage> capturedMessages = messageCaptor.getAllValues();

        SimpleMailMessage studentMessage = capturedMessages.get(0);
        assertEquals("student@example.com", studentMessage.getTo()[0]);
        assertTrue(studentMessage.getText().contains("Mentor LastName"));
        assertTrue(studentMessage.getText().contains("Tutoría semanal"));

        SimpleMailMessage mentorMessage = capturedMessages.get(1);
        assertEquals("mentor@example.com", mentorMessage.getTo()[0]);
        assertTrue(mentorMessage.getText().contains("Student LastName"));
        assertTrue(mentorMessage.getText().contains("Tutoría semanal"));
    }

    @Test
    void sendRecordatoryHandlesMultipleMeetings() {
        LocalDate today = LocalDate.now();

        UserEntity student1 = new UserEntity();
        student1.setFirstName("Student1");
        student1.setLastName("LastName1");
        student1.setEmail("student1@example.com");

        UserEntity mentor1 = new UserEntity();
        mentor1.setFirstName("Mentor1");
        mentor1.setLastName("LastName1");
        mentor1.setEmail("mentor1@example.com");

        MeetingEntity meeting1 = new MeetingEntity();
        meeting1.setDate(today);
        LocalTime now = LocalTime.now();
        meeting1.setTime(now);
        meeting1.setReason("Tutoría Java");
        meeting1.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
        meeting1.setStudent(student1);
        meeting1.setMentor(mentor1);

        UserEntity student2 = new UserEntity();
        student2.setFirstName("Student2");
        student2.setLastName("LastName2");
        student2.setEmail("student2@example.com");

        UserEntity mentor2 = new UserEntity();
        mentor2.setFirstName("Mentor2");
        mentor2.setLastName("LastName2");
        mentor2.setEmail("mentor2@example.com");

        MeetingEntity meeting2 = new MeetingEntity();
        meeting2.setDate(today);
        meeting2.setTime(now);
        meeting2.setReason("Revisión de proyecto");
        meeting2.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
        meeting2.setStudent(student2);
        meeting2.setMentor(mentor2);

        List<MeetingEntity> todaysMeetings = List.of(meeting1, meeting2);
        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
                .thenReturn(todaysMeetings);

        recordatorioMeetingService.sendRecordatory();

        verify(mailSender, times(4)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendRecordatoryDoesNothingWhenNoMeetingsForToday() {
        LocalDate today = LocalDate.now();
        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
                .thenReturn(List.of());

        recordatorioMeetingService.sendRecordatory();

        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendRecordatorySkipsEmailWhenStudentEmailIsNull() {
        LocalDate today = LocalDate.now();

        UserEntity student = new UserEntity();
        student.setFirstName("Student");
        student.setLastName("LastName");
        student.setEmail(null); // Email nulo

        UserEntity mentor = new UserEntity();
        mentor.setFirstName("Mentor");
        mentor.setLastName("LastName");
        mentor.setEmail("mentor@example.com");

        MeetingEntity meeting = new MeetingEntity();
        meeting.setDate(today);
        LocalTime now = LocalTime.now();
        meeting.setTime(now);
        meeting.setReason("Tutoría semanal");
        meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
        meeting.setStudent(student);
        meeting.setMentor(mentor);

        List<MeetingEntity> todaysMeetings = List.of(meeting);
        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
                .thenReturn(todaysMeetings);

        recordatorioMeetingService.sendRecordatory();

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage mentorMessage = messageCaptor.getValue();
        assertEquals("mentor@example.com", mentorMessage.getTo()[0]);
        assertTrue(mentorMessage.getText().contains("Student LastName"));
    }

    @Test
    void sendRecordatorySkipsEmailWhenMentorEmailIsNull() {
        LocalDate today = LocalDate.now();

        UserEntity student = new UserEntity();
        student.setFirstName("Student");
        student.setLastName("LastName");
        student.setEmail("student@example.com");

        UserEntity mentor = new UserEntity();
        mentor.setFirstName("Mentor");
        mentor.setLastName("LastName");
        mentor.setEmail(null); // Email nulo

        MeetingEntity meeting = new MeetingEntity();
        meeting.setDate(today);
        LocalTime now = LocalTime.now();
        meeting.setTime(now);
        meeting.setReason("Tutoría semanal");
        meeting.setStatus(MeetingEntity.MeetingStatus.ACCEPTED);
        meeting.setStudent(student);
        meeting.setMentor(mentor);

        List<MeetingEntity> todaysMeetings = List.of(meeting);
        when(meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED))
                .thenReturn(todaysMeetings);

        recordatorioMeetingService.sendRecordatory();

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage studentMessage = messageCaptor.getValue();
        assertEquals("student@example.com", studentMessage.getTo()[0]);
        assertTrue(studentMessage.getText().contains("Mentor LastName"));
    }

}