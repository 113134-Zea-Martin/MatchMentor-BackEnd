package com.scaffold.template.dtos.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.MeetingEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class MeetingHistoryResponseDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;

    private int duration; // Duración de la reunión en minutos

    private String otherParticipantName; // Nombre del otro participante
//    private String StudentName; // Nombre del estudiante
//    private String MentorName; // Nombre del mentor

    private String myRole; // Rol del usuario en la reunión (estudiante o mentor)
    private String reason;
    private MeetingEntity.MeetingStatus status;
}
