package com.scaffold.template.dtos.auth.login;

import com.scaffold.template.entities.Role;
import lombok.Data;

@Data
public class LoginResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private boolean isActive;
    private String token;
}