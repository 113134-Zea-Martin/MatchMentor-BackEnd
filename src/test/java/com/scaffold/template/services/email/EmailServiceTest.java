package com.scaffold.template.services.email;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendsSuccessfulRegistrationEmailWithCorrectDetails() {
        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO();
        userRegisterRequestDTO.setEmail("test@example.com");
        userRegisterRequestDTO.setFirstName("John");

        emailService.sendSuccessfulRegistrationEmail(userRegisterRequestDTO);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertThat(message.getTo()).contains("test@example.com");
        assertThat(message.getSubject()).isEqualTo("¡Bienvenido a Mentor Match, John!");
        assertThat(message.getText()).contains("Hola John");
    }

    @Test
    void sendsMatchRequestEmailToTutor() {
        emailService.sendMatchRequestEmail("tutor@example.com", "Student Name");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertThat(message.getTo()).contains("tutor@example.com");
        assertThat(message.getSubject()).isEqualTo("Solicitud de Match Recibida");
        assertThat(message.getText()).contains("Has recibido una solicitud de match de Student Name.");
    }

    @Test
    void sendsMatchAcceptedEmailToStudent() {
        emailService.sendMatchAcceptedEmail("student@example.com", "Tutor Name");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertThat(message.getTo()).contains("student@example.com");
        assertThat(message.getSubject()).isEqualTo("Solicitud de Match Aceptada");
        assertThat(message.getText()).contains("Tu solicitud de match ha sido aceptada por Tutor Name.");
    }

    @Test
    void sendsMeetingCreatedEmailWithCorrectDetails() {
        emailService.sendMeetingCreatedEmail("student@example.com", "Tutor Name", "2023-10-01", "10:00 AM");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertThat(message.getTo()).contains("student@example.com");
        assertThat(message.getSubject()).isEqualTo("Reunión Programada");
        assertThat(message.getText()).contains("Tu tutor Tutor Name ha programado una reunión para ti.");
        assertThat(message.getText()).contains("Fecha: 2023-10-01");
        assertThat(message.getText()).contains("Hora: 10:00 AM");
    }

    @Test
    void sendsMeetingResponseEmailWithAcceptedStatus() {
        emailService.sendMeetingResponseEmail("tutor@example.com", "Student Name", true);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertThat(message.getTo()).contains("tutor@example.com");
        assertThat(message.getSubject()).isEqualTo("Respuesta a la Solicitud de Reunión");
        assertThat(message.getText()).contains("El estudiante Student Name ha aceptado tu solicitud de reunión.");
    }

    @Test
    void sendsMeetingResponseEmailWithRejectedStatus() {
        emailService.sendMeetingResponseEmail("tutor@example.com", "Student Name", false);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();

        assertThat(message.getTo()).contains("tutor@example.com");
        assertThat(message.getSubject()).isEqualTo("Respuesta a la Solicitud de Reunión");
        assertThat(message.getText()).contains("El estudiante Student Name ha rechazado tu solicitud de reunión.");
    }
}