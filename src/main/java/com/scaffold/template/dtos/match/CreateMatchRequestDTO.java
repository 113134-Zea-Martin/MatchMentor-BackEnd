package com.scaffold.template.dtos.match;

import lombok.Data;

@Data
public class CreateMatchRequestDTO {

    private Long studentId;
    private Long tutorId;
    private boolean isLiked;
}
