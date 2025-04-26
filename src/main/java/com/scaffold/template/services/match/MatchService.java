package com.scaffold.template.services.match;

import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MatchService {
    List<UserEntity> findCompatibleTutors(Long userId);
    List<Long> getIDsOfTutorsWithCommonInterests(Long userId);
    UserResponseDTO getTutorCompatibleWithStudent(Long studentId);
}
