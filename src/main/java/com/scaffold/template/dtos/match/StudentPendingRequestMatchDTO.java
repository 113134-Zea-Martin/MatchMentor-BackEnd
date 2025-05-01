package com.scaffold.template.dtos.match;

import lombok.Data;

import java.util.List;

@Data
public class StudentPendingRequestMatchDTO {

    private Long id;
    private Long studentId;
    private String firstName;
    private String lastName;
    private String studyArea;
    private String educationLevel;
    private String mentoringGoals;
    private List<String> studentInterests;
}
