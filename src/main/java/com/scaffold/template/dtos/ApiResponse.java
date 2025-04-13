package com.scaffold.template.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {
    /**
     * Indicates whether the request was successful or not.
     */
    private Boolean success;

    /**
     * Contains the data returned from the API.
     */
    private Object data;

    /**
     * Contains any error messages returned from the API.
     */
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
