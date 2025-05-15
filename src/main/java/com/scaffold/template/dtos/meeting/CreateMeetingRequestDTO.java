package com.scaffold.template.dtos.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateMeetingRequestDTO {
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Mentor ID cannot be null")
    private Long mentorId;

    @NotNull(message = "Match ID cannot be null")
    private Long matchId;

    @NotNull(message = "Date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(type = "string", pattern = "HH:mm", example = "14:30")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @NotNull(message = "Time cannot be null")
    private LocalTime time;

    @Min(value = 1, message = "Duration must be positive")
    @NotNull(message = "Duration cannot be null")
    private int duration;

    @NotEmpty(message = "Reason cannot be empty")
    private String reason;
}