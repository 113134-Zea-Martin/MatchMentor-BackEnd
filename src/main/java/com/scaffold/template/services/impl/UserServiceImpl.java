package com.scaffold.template.services.impl;

import com.scaffold.template.dtos.interest.InterestRequestDTO;
import com.scaffold.template.dtos.profile.StudentResponseDTO;
import com.scaffold.template.dtos.profile.TutorResponseDTO;
import com.scaffold.template.dtos.profile.UpdateUserRequestDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.InterestEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserInterestEntity;
import com.scaffold.template.repositories.InterestRepository;
import com.scaffold.template.repositories.UserInterestRepository;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.repositories.UserViewedProfileRepository;
import com.scaffold.template.security.JwtConfig;
import com.scaffold.template.services.InterestService;
import com.scaffold.template.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public final JwtConfig jwtConfig;
    public final UserInterestRepository userInterestRepository;
    public final InterestService interestService;
    private final UserViewedProfileRepository userViewedProfileRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository
            , JwtConfig jwtConfig
            , UserInterestRepository userInterestRepository
            , InterestService interestService
            , UserViewedProfileRepository userViewedProfileRepository) {
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.userInterestRepository = userInterestRepository;
        this.interestService = interestService;
        this.userViewedProfileRepository = userViewedProfileRepository;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userEntity.getId());
        userResponseDTO.setFirstName(userEntity.getFirstName());
        userResponseDTO.setLastName(userEntity.getLastName());
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setBirthDate(userEntity.getBirthDate());
        userResponseDTO.setLocation(userEntity.getLocation());
        userResponseDTO.setEducationLevel(userEntity.getEducationLevel());
        userResponseDTO.setStudyArea(userEntity.getStudyArea());
        userResponseDTO.setInstitution(userEntity.getInstitution());
        userResponseDTO.setGraduationYear(userEntity.getGraduationYear());
        userResponseDTO.setMentoringGoals(userEntity.getMentoringGoals());
        userResponseDTO.setCurrentProfession(userEntity.getCurrentProfession());
        userResponseDTO.setCompany(userEntity.getCompany());
        userResponseDTO.setYearsOfExperience(userEntity.getYearsOfExperience());
        userResponseDTO.setProfessionalBio(userEntity.getProfessionalBio());
        userResponseDTO.setHourlyRate(userEntity.getHourlyRate());
        userResponseDTO.setIsVisible(userEntity.getIsVisible());

        List<UserInterestEntity> userInterests = userEntity.getUserInterests();
        List<String> interests = new ArrayList<>();

        for (UserInterestEntity userInterest : userInterests) {
            interests.add(userInterest.getInterest().getName());
        }

        userResponseDTO.setInterests(interests);
        userResponseDTO.setRole(userEntity.getRole());
        userResponseDTO.setLinkedinUrl(userEntity.getLinkedinUrl());
        userResponseDTO.setBio(userEntity.getBio());
        userResponseDTO.setCreatedAt(userEntity.getCreatedAt());
        userResponseDTO.setIsActive(userEntity.getIsActive());

        return userResponseDTO;

    }

    @Override
    public UserResponseDTO getUserInfoByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userEntity.getId());
        userResponseDTO.setFirstName(userEntity.getFirstName());
        userResponseDTO.setLastName(userEntity.getLastName());
        userResponseDTO.setEmail(userEntity.getEmail());
        userResponseDTO.setRole(userEntity.getRole());
        userResponseDTO.setIsActive(userEntity.getIsActive());

        return userResponseDTO;
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        UserResponseDTO userResponseDTO = getUserById(id);
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
        studentResponseDTO.setId(userResponseDTO.getId());
        studentResponseDTO.setFirstName(userResponseDTO.getFirstName());
        studentResponseDTO.setLastName(userResponseDTO.getLastName());
        studentResponseDTO.setEmail(userResponseDTO.getEmail());
        studentResponseDTO.setBirthDate(userResponseDTO.getBirthDate());
        studentResponseDTO.setLocation(userResponseDTO.getLocation());
        studentResponseDTO.setEducationLevel(userResponseDTO.getEducationLevel());
        studentResponseDTO.setStudyArea(userResponseDTO.getStudyArea());
        studentResponseDTO.setInstitution(userResponseDTO.getInstitution());
        studentResponseDTO.setGraduationYear(userResponseDTO.getGraduationYear());
        studentResponseDTO.setMentoringGoals(userResponseDTO.getMentoringGoals());
        studentResponseDTO.setInterests(userResponseDTO.getInterests());
        studentResponseDTO.setLinkedinUrl(userResponseDTO.getLinkedinUrl());
        studentResponseDTO.setBio(userResponseDTO.getBio());
        studentResponseDTO.setCreatedAt(userResponseDTO.getCreatedAt());
        studentResponseDTO.setIsActive(userResponseDTO.getIsActive());

        return studentResponseDTO;
    }

    @Override
    public TutorResponseDTO getTutorById(Long id) {
        UserResponseDTO userResponseDTO = getUserById(id);
        TutorResponseDTO tutorResponseDTO = new TutorResponseDTO();
        tutorResponseDTO.setId(userResponseDTO.getId());
        tutorResponseDTO.setFirstName(userResponseDTO.getFirstName());
        tutorResponseDTO.setLastName(userResponseDTO.getLastName());
        tutorResponseDTO.setEmail(userResponseDTO.getEmail());
        tutorResponseDTO.setBirthDate(userResponseDTO.getBirthDate());
        tutorResponseDTO.setLocation(userResponseDTO.getLocation());
        tutorResponseDTO.setCurrentProfession(userResponseDTO.getCurrentProfession());
        tutorResponseDTO.setCompany(userResponseDTO.getCompany());
        tutorResponseDTO.setYearsOfExperience(userResponseDTO.getYearsOfExperience());
        tutorResponseDTO.setProfessionalBio(userResponseDTO.getProfessionalBio());
        tutorResponseDTO.setHourlyRate(userResponseDTO.getHourlyRate());
        tutorResponseDTO.setInterests(userResponseDTO.getInterests());
        tutorResponseDTO.setLinkedinUrl(userResponseDTO.getLinkedinUrl());
        tutorResponseDTO.setBio(userResponseDTO.getBio());
        tutorResponseDTO.setCreatedAt(userResponseDTO.getCreatedAt());
        tutorResponseDTO.setIsActive(userResponseDTO.getIsActive());
        tutorResponseDTO.setIsVisible(userResponseDTO.getIsVisible());
        return tutorResponseDTO;
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

//        for (UserInterestEntity userInterest : userEntity.getUserInterests()) {
//            userInterestRepository.deleteByUserIdAndInterestId(userEntity.getId(), userInterest.getInterest().getId());
//        }
        userEntity.getUserInterests().clear();

        userEntity.setFirstName(updateUserRequestDTO.getFirstName());
        userEntity.setLastName(updateUserRequestDTO.getLastName());
        userEntity.setEmail(updateUserRequestDTO.getEmail());
        userEntity.setBirthDate(updateUserRequestDTO.getBirthDate());
        userEntity.setLocation(updateUserRequestDTO.getLocation());
        userEntity.setEducationLevel(updateUserRequestDTO.getEducationLevel());
        userEntity.setStudyArea(updateUserRequestDTO.getStudyArea());
        userEntity.setInstitution(updateUserRequestDTO.getInstitution());
        userEntity.setGraduationYear(updateUserRequestDTO.getGraduationYear());
        userEntity.setMentoringGoals(updateUserRequestDTO.getMentoringGoals());
        userEntity.setCurrentProfession(updateUserRequestDTO.getCurrentProfession());
        userEntity.setCompany(updateUserRequestDTO.getCompany());
        userEntity.setYearsOfExperience(updateUserRequestDTO.getYearsOfExperience());
        userEntity.setProfessionalBio(updateUserRequestDTO.getProfessionalBio());
        userEntity.setHourlyRate(updateUserRequestDTO.getHourlyRate());
        userEntity.setIsVisible(updateUserRequestDTO.getIsVisible());

        for (String interest : updateUserRequestDTO.getInterests()) {
            InterestEntity interestEntity = interestService.getByName(interest);
            if (interestEntity == null) {
                InterestRequestDTO interestRequestDTO = new InterestRequestDTO();
                interestRequestDTO.setName(interest);
                interestEntity = interestService.createInterest(interestRequestDTO);
            }
            UserInterestEntity userInterestEntity = new UserInterestEntity();
            userInterestEntity.setUser(userEntity);
            userInterestEntity.setInterest(interestEntity);
            userInterestEntity.setCreatedAt(userEntity.getCreatedAt());
            userInterestEntity.setActive(true);
            userEntity.getUserInterests().add(userInterestEntity);
        }

//        userEntity.setUserInterests(userInterests);
        userEntity.setRole(updateUserRequestDTO.getRole());
        userEntity.setLinkedinUrl(updateUserRequestDTO.getLinkedinUrl());
        userEntity.setBio(updateUserRequestDTO.getBio());
        userEntity.setCreatedAt(userEntity.getCreatedAt());
        userEntity.setIsActive(updateUserRequestDTO.getIsActive());

        UserEntity updatedUser = userRepository.save(userEntity);

        // Limpiamos los perfiles visualizados
        userViewedProfileRepository.deleteByViewerId(userEntity.getId());


        return getUserById(updatedUser.getId());
    }
}
