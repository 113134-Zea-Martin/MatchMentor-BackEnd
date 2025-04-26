package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.interest.InterestRequestDTO;
import com.scaffold.template.entities.InterestEntity;
import com.scaffold.template.services.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestService interestService;

    @Autowired
    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getInterests() {
        List<InterestRequestDTO> interests = interestService.getAllInterests();
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setStatusCode(200);
        response.setData(interests);
        response.setMessage("Interests retrieved successfully");
        response.setTimestamp(LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

}
