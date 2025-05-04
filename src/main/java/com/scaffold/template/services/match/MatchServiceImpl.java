package com.scaffold.template.services.match;

import com.scaffold.template.dtos.match.ConfirmedMatchResponseDTO;
import com.scaffold.template.dtos.match.MatchResponseDTO;
import com.scaffold.template.dtos.match.StudentPendingRequestMatchDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.Status;
import com.scaffold.template.repositories.MatchRepository;
import com.scaffold.template.services.UserService;
import com.scaffold.template.services.chat.ChatService;
import com.scaffold.template.services.email.EmailService;
import com.scaffold.template.services.notification.NotificationService;
import com.scaffold.template.services.userViewedProfileService.UserViewedProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final UserService userService;
    private final UserViewedProfileService userViewedProfileService;
    private final NotificationService notificationService;
    private final ChatService chatService;
    private final EmailService emailService;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository,
                            UserService userService,
                            UserViewedProfileService userViewedProfileService,
                            NotificationService notificationService,
                            ChatService chatService,
                            EmailService emailService) {
        this.matchRepository = matchRepository;
        this.userService = userService;
        this.userViewedProfileService = userViewedProfileService;
        this.notificationService = notificationService;
        this.chatService = chatService;
        this.emailService = emailService;
    }

    @Override
    public List<Long> getIDsOfTutorsWithCommonInterests(Long userId) {
//        List<UserEntity> tutors = userRepository.findTutorsWithCommonInterests(userId);
        List<UserEntity> tutors = userService.findTutorsWithCommonInterests(userId);
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

    @Override
    public MatchEntity createMatch(Long studentId, Long tutorId, boolean isAccepted) {
        UserEntity student = userService.getUserEntityById(studentId);
        UserEntity tutor = userService.getUserEntityById(tutorId);

        userViewedProfileService.createUserViewedProfile(student, tutor, isAccepted);

        MatchEntity match = new MatchEntity();
        match.setStudent(student);
        match.setTutor(tutor);
        match.setStatus(isAccepted ? Status.PENDING : Status.REJECTED);
        match.setCreatedAt(LocalDateTime.now());
        MatchEntity savedMatch = matchRepository.save(match);

        if (isAccepted) {
            notificationService.createNotificationConnectionRequest(
                    tutorId,
                    student.getFirstName(),
                    savedMatch.getId()
            );
            emailService.sendMatchRequestEmail(tutor.getEmail(), student.getFirstName());
        }

        return savedMatch;
    }

    @Override
    public MatchResponseDTO mapToMatchResponseDTO(MatchEntity matchEntity) {
        MatchResponseDTO matchResponseDTO = new MatchResponseDTO();
        matchResponseDTO.setId(matchEntity.getId());
        matchResponseDTO.setStudentId(matchEntity.getStudent().getId());
        matchResponseDTO.setTutorId(matchEntity.getTutor().getId());
        matchResponseDTO.setStatus(matchEntity.getStatus());
        matchResponseDTO.setCreatedAt(matchEntity.getCreatedAt());
        matchResponseDTO.setUpdatedAt(matchEntity.getUpdatedAt());
        return matchResponseDTO;
    }

    @Override
    public List<MatchEntity> getMatchesByStatusAndTutorId(Status status, Long tutorId) {
        return matchRepository.findByStatusAndTutorId(status, tutorId);
    }

    @Override
    public List<StudentPendingRequestMatchDTO> mapToStudentPendingRequestMatchDTOs(List<MatchEntity> matches) {
        return matches.stream()
                .map(match -> {
                    StudentPendingRequestMatchDTO dto = new StudentPendingRequestMatchDTO();
                    dto.setId(match.getId());
                    dto.setStudentId(match.getStudent().getId());
                    dto.setFirstName(match.getStudent().getFirstName());
                    dto.setLastName(match.getStudent().getLastName());
                    dto.setStudyArea(match.getStudent().getStudyArea());
                    dto.setEducationLevel(match.getStudent().getEducationLevel());
                    dto.setMentoringGoals(match.getStudent().getMentoringGoals());
                    List<String> interests = new ArrayList<>();

                    match.getStudent().getUserInterests()
                            .forEach(interest -> interests.add(interest.getInterest().getName()));
                    dto.setStudentInterests(interests);

                    return dto;
                })
                .toList();
    }

    public MatchEntity getMatchById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));
    }

    @Override
    public MatchEntity acceptMatch(Long matchId, boolean isAccepted) {
        MatchEntity match = getMatchById(matchId);
        match.setStatus(isAccepted ? Status.ACCEPTED : Status.REJECTED);
        match.setUpdatedAt(LocalDateTime.now());

        // Create a welcome chat when the match is accepted
        if (isAccepted) {
//            chatService.createWelcomeChat(matchId);
            emailService.sendMatchAcceptedEmail(match.getStudent().getEmail(), match.getTutor().getFirstName());
        }

        return matchRepository.save(match);
    }

    @Override
    public List<MatchEntity> getMatchesByStatusAndUserId(Status status, Long userId) {
        return matchRepository.findByStatusAndUserId(status, userId);
    }

    @Override
    public ConfirmedMatchResponseDTO mapToConfirmedMatchResponseDTO(MatchEntity matchEntity, Long userId) {

        LocalDateTime lastMessageDate = chatService.getLastMessageDate(matchEntity.getId());

        ConfirmedMatchResponseDTO confirmedMatchResponseDTO = new ConfirmedMatchResponseDTO();
        confirmedMatchResponseDTO.setId(matchEntity.getId());
        Long responseUserId = Objects.equals(userId, matchEntity.getStudent().getId()) ? matchEntity.getTutor().getId() : matchEntity.getStudent().getId();
        confirmedMatchResponseDTO.setUserId(responseUserId);

        UserEntity responseUser = userService.getUserEntityById(responseUserId);

        confirmedMatchResponseDTO.setFullName(responseUser.getFirstName() + " " + responseUser.getLastName());
//        confirmedMatchResponseDTO.setRole(responseUser.getRole());

        if (Objects.equals(userId, matchEntity.getStudent().getId())) {
            confirmedMatchResponseDTO.setRole(Role.TUTOR);
        } else {
            confirmedMatchResponseDTO.setRole(Role.STUDENT);
        }

        if (!Objects.equals(userId, matchEntity.getStudent().getId())) {
            confirmedMatchResponseDTO.setDescription(responseUser.getStudyArea());
        } else {
            confirmedMatchResponseDTO.setDescription(responseUser.getCurrentProfession());
        }

        confirmedMatchResponseDTO.setUpdatedAt(matchEntity.getUpdatedAt());
        confirmedMatchResponseDTO.setIsActive(responseUser.getIsActive());

        if (lastMessageDate != null) {
            confirmedMatchResponseDTO.setLastMessageDate(lastMessageDate);
        } else {
            confirmedMatchResponseDTO.setLastMessageDate(matchEntity.getCreatedAt());
        }

        Boolean existsUnreadMessage = chatService.existsByMatchIdAndSenderIdAndIsReadFalse(matchEntity.getId(), responseUserId);

        confirmedMatchResponseDTO.setHasUnreadMessages(existsUnreadMessage);

        return confirmedMatchResponseDTO;
    }

}
