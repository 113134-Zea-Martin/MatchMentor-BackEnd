package com.scaffold.template.services.match;

import com.scaffold.template.dtos.match.MatchResponseDTO;
import com.scaffold.template.dtos.match.StudentPendingRequestMatchDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Status;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MatchService {
//    List<UserEntity> findCompatibleTutors(Long userId);
    List<Long> getIDsOfTutorsWithCommonInterests(Long userId);
    UserResponseDTO getTutorCompatibleWithStudent(Long studentId);
    MatchEntity createMatch(Long studentId, Long tutorId, boolean isAccepted);
    MatchResponseDTO mapToMatchResponseDTO(MatchEntity matchEntity);
    List<MatchEntity> getMatchesByStatusAndTutorId(Status status, Long tutorId);
//    List<StudentPendingRequestMatchDTO> getMatchesByStatusAndTutorId(Status status, Long tutorId);
    List<StudentPendingRequestMatchDTO> mapToStudentPendingRequestMatchDTOs(List<MatchEntity> matches);
}
