package com.scaffold.template.services;

import com.scaffold.template.dtos.UserRegisterRequestDTO;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.models.Dummy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    List<UserEntity> getAllUsers();
    UserEntity registerStudent(UserRegisterRequestDTO userRegisterRequestDTO);
}
