package com.scaffold.template.services.email;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendSuccessfulRegistrationEmail(UserRegisterRequestDTO userRegisterRequestDTO) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(userRegisterRequestDTO.getEmail());
        mensaje.setSubject("¡Bienvenido a Mentor Match, " + userRegisterRequestDTO.getFirstName() + "!");
        mensaje.setText(
                "Hola " + userRegisterRequestDTO.getFirstName() + ",\n\n" +
                        "Tu registro en Mentor Match se completó con éxito!\n\n" +
                        "Ya puedes iniciar sesión y comenzar a explorar.\n\n"  +
                        "Si tienes alguna duda, no dudes en contactarnos.\n\n" +
                        "¡Te deseamos mucho éxito en tu busqueda!\n\n" +
                        "El equipo de Mentor Match\n");

        mailSender.send(mensaje);
    }

    // Método para enviar un correo electrónico al tutor indicando que ha recibido una solicitud de match de un estudiante
    @Async
    public void sendMatchRequestEmail(String tutorEmail, String studentName) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(tutorEmail);
        mensaje.setSubject("Solicitud de Match Recibida");
        mensaje.setText(
                "Hola,\n\n" +
                        "Has recibido una solicitud de match de " + studentName + ".\n\n" +
                        "Inicia sesión en tu cuenta para revisar la solicitud.\n\n" +
                        "¡Gracias por ser parte de Mentor Match!\n\n" +
                        "El equipo de Mentor Match\n");

        mailSender.send(mensaje);
    }

    // Método para enviar un correo electrónico al estudiante indicando que su solicitud de match ha sido aceptada.
    @Async
    public void sendMatchAcceptedEmail(String studentEmail, String tutorName) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(studentEmail);
        mensaje.setSubject("Solicitud de Match Aceptada");
        mensaje.setText(
                "Hola,\n\n" +
                        "Tu solicitud de match ha sido aceptada por " + tutorName + ".\n\n" +
                        "Inicia sesión en tu cuenta para ver los detalles.\n\n" +
                        "¡Gracias por ser parte de Mentor Match!\n\n" +
                        "El equipo de Mentor Match\n");

        mailSender.send(mensaje);
    }

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(to);
        mensaje.setSubject(subject);
        mensaje.setText(body);
        mailSender.send(mensaje);
    }

    // Método para enviar un correo electrónico al estudiante indicando que el tutor ha creado una reunión.
    @Async
    public void sendMeetingCreatedEmail(String studentEmail, String tutorName, String meetingDate, String meetingTime) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(studentEmail);
        mensaje.setSubject("Reunión Programada");
        mensaje.setText(
                "Hola,\n\n" +
                        "Tu tutor " + tutorName + " ha programado una reunión para ti.\n\n" +
                        "Fecha: " + meetingDate + "\n" +
                        "Hora: " + meetingTime + "\n\n" +
                        "Inicia sesión en tu cuenta para ver los detalles.\n\n" +
                        "¡Gracias por ser parte de Mentor Match!\n\n" +
                        "El equipo de Mentor Match\n");

        mailSender.send(mensaje);
    }
}