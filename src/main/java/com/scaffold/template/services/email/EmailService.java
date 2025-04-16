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

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(to);
        mensaje.setSubject(subject);
        mensaje.setText(body);
        mailSender.send(mensaje);
    }
}