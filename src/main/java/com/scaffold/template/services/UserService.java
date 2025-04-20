package com.scaffold.template.services;

import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserInfoByEmail(String email);
}
