package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.services.match.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

//    @GetMapping("/{userId}/compatible-tutors")
//    public ResponseEntity<List<UserEntity>> findCompatibleTutors(@RequestParam Long userId) {
//
//        List<UserEntity> compatibleTutors = matchService.findCompatibleTutors(userId);
//        return ResponseEntity.ok(compatibleTutors);
//    }

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

}
