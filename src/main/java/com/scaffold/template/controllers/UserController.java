package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.profile.UserInfoDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ApiResponse> getUserProfileById(@PathVariable Long profileId) {

        try {
            UserResponseDTO userResponseDTO = userService.getUserById(profileId);
            ApiResponse response = ApiResponse.builder()
                    .success(true)
                    .statusCode(200)
                    .data(userResponseDTO)
                    .message("User profile retrieved successfully")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .statusCode(401)
                    .message("Error: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(200).body(response);
        }

    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        UserResponseDTO userResponseDTO = userService.getUserInfoByEmail(username);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setFirstName(userResponseDTO.getFirstName());
        userInfoDTO.setLastName(userResponseDTO.getLastName());
        userInfoDTO.setRole(userResponseDTO.getRole().toString());
        userInfoDTO.setId(userResponseDTO.getId());
        userInfoDTO.setActive(userResponseDTO.getIsActive());
        userInfoDTO.setEmail(userResponseDTO.getEmail());
        userInfoDTO.setSub(userDetails.getUsername());
        userInfoDTO.setIat(System.currentTimeMillis() / 1000L);
//        userInfoDTO.setExp(userResponseDTO.getCreatedAt().getTime() / 1000L);
//        userInfoDTO.setExp(userResponseDTO.getCreatedAt().toInstant(ZoneOffset.UTC).getEpochSecond());
//        userInfoDTO.setAuthorities(userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList()));
        return ResponseEntity.ok(userInfoDTO);
    }

}
