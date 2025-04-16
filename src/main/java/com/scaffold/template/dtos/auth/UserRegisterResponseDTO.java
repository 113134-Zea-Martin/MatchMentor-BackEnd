package com.scaffold.template.dtos.auth;

import lombok.Data;

@Data
public class UserRegisterResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String interests;
}
