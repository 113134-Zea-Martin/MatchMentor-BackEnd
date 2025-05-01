package com.scaffold.template.services;

import com.scaffold.template.dtos.profile.StudentResponseDTO;
import com.scaffold.template.dtos.profile.TutorResponseDTO;
import com.scaffold.template.dtos.profile.UpdateUserRequestDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserInfoByEmail(String email);
    StudentResponseDTO getStudentById(Long id);
    TutorResponseDTO getTutorById(Long id);
    UserResponseDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO);
    UserEntity getUserEntityById(Long id);
    List<UserEntity> findTutorsWithCommonInterests(Long userId);
}
