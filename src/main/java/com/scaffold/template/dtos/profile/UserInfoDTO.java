package com.scaffold.template.dtos.profile;

import lombok.Data;

import java.util.List;
// DTO para obtener información del usuario desde el token JWT
@Data
public class UserInfoDTO {
    private String firstName;
    private String lastName;
    private String role;
    private Long id;
    private boolean isActive;
    private String email;
    private String sub;
    private long iat;
    private long exp;
}
