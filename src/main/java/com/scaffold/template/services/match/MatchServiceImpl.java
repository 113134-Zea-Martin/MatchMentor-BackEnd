package com.scaffold.template.services.match;

import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public MatchServiceImpl(UserRepository userRepository
            , UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public List<UserEntity> findCompatibleTutors(Long userId) {
        return userRepository.findTutorsWithCommonInterests(userId);
    }

    @Override
    public List<Long> getIDsOfTutorsWithCommonInterests(Long userId) {
        List<UserEntity> tutors = userRepository.findTutorsWithCommonInterests(userId);
        return tutors.stream()
                .map(UserEntity::getId)
                .toList();
    }

    @Override
    public UserResponseDTO getTutorCompatibleWithStudent(Long studentId) {
        List<Long> tutorIds = getIDsOfTutorsWithCommonInterests(studentId);
        if (tutorIds.isEmpty()) {
            return null; // No compatible tutors found
        }
        Long tutorId = tutorIds.get(0); // Get the first compatible tutor
        return userService.getUserById(tutorId);
    }

}
