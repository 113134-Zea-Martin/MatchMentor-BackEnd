package com.scaffold.template.dtos.match;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchResponseDTO {

    private Long id;
    private Long studentId;
    private Long tutorId;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
