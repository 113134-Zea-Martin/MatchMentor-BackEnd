package com.scaffold.template.services;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginRequestDTO;
import com.scaffold.template.dtos.auth.login.LoginResponseDTO;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    List<UserEntity> getAllUsers();
    UserEntity registerUser(UserRegisterRequestDTO userRegisterRequestDTO);
    UserEntity authenticateUser(String email, String password);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    UserEntity getUserByEmail(String email);
}
