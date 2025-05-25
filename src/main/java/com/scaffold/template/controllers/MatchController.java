package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.match.ConfirmedMatchResponseDTO;
import com.scaffold.template.dtos.match.CreateMatchRequestDTO;
import com.scaffold.template.dtos.match.MatchResponseDTO;
import com.scaffold.template.dtos.match.StudentPendingRequestMatchDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Status;
import com.scaffold.template.services.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{userId}/tutors-with-common-interests")
    public ResponseEntity<List<Long>> getIDsOfTutorsWithCommonInterests(@PathVariable Long userId) {
        List<Long> tutorIds = matchService.getIDsOfTutorsWithCommonInterests(userId);
        return ResponseEntity.ok(tutorIds);
    }

    @GetMapping("/{studentId}/tutor-compatible-with-student")
    public ResponseEntity<ApiResponse> getTutorCompatibleWithStudent(@PathVariable Long studentId) {
        UserResponseDTO tutor = matchService.getTutorCompatibleWithStudent(studentId);
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        if (tutor == null) {
            response.setSuccess(false);
            response.setStatusCode(404);
            response.setMessage("No se encontraron más tutores con intereses comunes");
            return ResponseEntity.status(200).body(response);
        }
        response.setSuccess(true);
        response.setStatusCode(200);
        response.setData(tutor);
        response.setMessage("Tutor compatible con el estudiante encontrado");
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> createMatch(@RequestBody CreateMatchRequestDTO request) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            MatchEntity match = matchService.createMatch(request.getStudentId(), request.getTutorId(), request.isLiked());
            MatchResponseDTO matchResponseDTO = matchService.mapToMatchResponseDTO(match);
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(matchResponseDTO);
            response.setMessage("Match creado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al crear el match: " + e.getMessage());
            return ResponseEntity.status(200).body(response);
        }
    }

    @GetMapping("/pending-request/{tutorId}")
    public ResponseEntity<List<StudentPendingRequestMatchDTO>> getPendingRequestMatches(@PathVariable Long tutorId) {
        List<MatchEntity> matches = matchService.getMatchesByStatusAndTutorId(Status.PENDING, tutorId);
        List<MatchEntity> matchesOrderedByIdDesc = matches.stream()
                .sorted((m1, m2) -> Long.compare(m2.getId(), m1.getId()))
                .toList();
        List<StudentPendingRequestMatchDTO> studentPendingRequestMatchDTOS = matchService.mapToStudentPendingRequestMatchDTOs(matchesOrderedByIdDesc);
        return ResponseEntity.ok(studentPendingRequestMatchDTOS);
    }

    @PutMapping("/accept/{matchId}")
    public ResponseEntity<ApiResponse> acceptMatch(@PathVariable Long matchId) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            MatchEntity match = matchService.acceptMatch(matchId, true);
            MatchResponseDTO matchResponseDTO = matchService.mapToMatchResponseDTO(match);
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(matchResponseDTO);
            response.setMessage("Match aceptado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al aceptar el match: " + e.getMessage());
            return ResponseEntity.status(200).body(response);
        }
    }

    @PutMapping("/reject/{matchId}")
    public ResponseEntity<ApiResponse> rejectMatch(@PathVariable Long matchId) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            MatchEntity match = matchService.acceptMatch(matchId, false);
            MatchResponseDTO matchResponseDTO = matchService.mapToMatchResponseDTO(match);
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(matchResponseDTO);
            response.setMessage("Match rechazado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al rechazar el match: " + e.getMessage());
            return ResponseEntity.status(200).body(response);
        }
    }

    @GetMapping("/confirmed/{userId}")
    public ResponseEntity<List<ConfirmedMatchResponseDTO>> getConfirmedMatches(@PathVariable Long userId) {
        List<MatchEntity> matches = matchService.getMatchesByStatusAndUserId(Status.ACCEPTED, userId);
        List<ConfirmedMatchResponseDTO> confirmedMatches = new ArrayList<>();
        for (MatchEntity match : matches) {
            ConfirmedMatchResponseDTO confirmedMatch = matchService.mapToConfirmedMatchResponseDTO(match, userId);
            if (confirmedMatch.getIsActive()) {
                confirmedMatches.add(confirmedMatch);
            }
        }
//        confirmedMatches.sort((m1, m2) -> Long.compare(m2.getId(), m1.getId()));
        confirmedMatches.sort((m1, m2) -> m2.getLastMessageDate().compareTo(m1.getLastMessageDate()));
        return ResponseEntity.ok(confirmedMatches);
    }

}
