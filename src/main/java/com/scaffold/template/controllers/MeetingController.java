package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.meeting.CreateMeetingRequestDTO;
import com.scaffold.template.dtos.meeting.MeetingHistoryResponseDTO;
import com.scaffold.template.services.meeting.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // Create a meeting
    @PostMapping("")
    public ResponseEntity<ApiResponse> createMeeting(@RequestBody @Validated CreateMeetingRequestDTO request) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            CreateMeetingRequestDTO meeting = meetingService.createMeeting(request.getStudentId(), request.getMentorId(), request.getMatchId(),
                    request.getDate(), request.getTime(), request.getDuration(), request.getReason());
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(meeting);
            response.setMessage("Reunión creada con éxito");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse> getMeetingHistory(@PathVariable Long userId) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            List<MeetingHistoryResponseDTO> meetingHistory = meetingService.getMeetingHistoryResponseDTO(userId);
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(meetingHistory);
            response.setMessage("Historial de reuniones obtenido con éxito");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(404);
            response.setMessage("Error: " + e.getMessage());
        }
        return ResponseEntity.status(200).body(response);
    }

    // Rechazar una reunión
    @PutMapping("/reject/{meetingId}")
    public ResponseEntity<ApiResponse> rejectMeeting(@PathVariable Long meetingId) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        boolean status = false;
        try {
            meetingService.respondToMeeting(meetingId, status);
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setMessage("Se rechazó la reunión con éxito");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return ResponseEntity.status(200).body(response);
    }
}
