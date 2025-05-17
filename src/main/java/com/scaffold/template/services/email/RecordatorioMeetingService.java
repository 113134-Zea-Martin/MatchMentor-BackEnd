package com.scaffold.template.services.email;


import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.repositories.MeetingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecordatorioMeetingService {

    private final MeetingRepository meetingRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public RecordatorioMeetingService(MeetingRepository meetingRepository, JavaMailSender mailSender) {
        this.meetingRepository = meetingRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(cron = "0 10 6 * * ?") // Ejecuta todos los días a las 6:10 AM
    @Transactional
    public void sendRecordatory() {
        LocalDate today = LocalDate.now();
        List<MeetingEntity> meetings = meetingRepository.findByDateAndStatus(today, MeetingEntity.MeetingStatus.ACCEPTED);

        for (MeetingEntity meeting : meetings) {
            sendMail(meeting.getStudent().getEmail(), meeting,
                    meeting.getMentor().getFirstName() + " " + meeting.getMentor().getLastName());

            sendMail(meeting.getMentor().getEmail(), meeting,
                    meeting.getStudent().getFirstName() + " " + meeting.getStudent().getLastName());
        }
    }


    private void sendMail(String destinatario, MeetingEntity meeting, String nombre) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("Recordatorio de reunión programada");
        message.setText("Tienes una reunión programada hoy a las " + meeting.getTime() +
                " con " + nombre + ".\n" +
                "Motivo: " + meeting.getReason() + "\n\n" +
                "Inicia sesión en tu cuenta para ver los detalles.\n\n" +
                "¡Gracias por ser parte de Mentor Match!\n\n" +
                "El equipo de Mentor Match\n");
        mailSender.send(message);
    }
}
