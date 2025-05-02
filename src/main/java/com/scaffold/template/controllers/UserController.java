package com.scaffold.template.controllers;

import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.profile.StudentResponseDTO;
import com.scaffold.template.dtos.profile.TutorResponseDTO;
import com.scaffold.template.dtos.profile.UpdateUserRequestDTO;
import com.scaffold.template.dtos.profile.UserInfoDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.Role;
import com.scaffold.template.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    @PutMapping("/{profileId}")
    public ResponseEntity<ApiResponse> updateUserProfile(@PathVariable Long profileId,
                                                         @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {

        // Logic to update the user profile goes here
        UserResponseDTO updatedUser = userService.updateUser(profileId, updateUserRequestDTO);
        try {
            ApiResponse response = ApiResponse.builder()
                    .success(true)
                    .statusCode(200)
                    .data(updatedUser)
                    .message("Se actualizó tu perfil correctamente")
                    .timestamp(LocalDateTime.now())
                    .build();

            return ResponseEntity.ok(response);
            // Assuming userService.updateUser returns the updated user
        } catch (Exception e) {
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .statusCode(400)
                    .message("Error: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    }

//    @GetMapping("/me")
//    public ResponseEntity<UserInfoDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
//        if (userDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        String email = userDetails.getUsername();
//        UserResponseDTO userResponseDTO = userService.getUserInfoByEmail(email);
//        UserInfoDTO userInfoDTO = new UserInfoDTO();
//        userInfoDTO.setFirstName(userResponseDTO.getFirstName());
//        userInfoDTO.setLastName(userResponseDTO.getLastName());
//        userInfoDTO.setRole(userResponseDTO.getRole().toString());
//        userInfoDTO.setId(userResponseDTO.getId());
//        userInfoDTO.setActive(userResponseDTO.getIsActive());
//        userInfoDTO.setEmail(userResponseDTO.getEmail());
//        userInfoDTO.setSub(userDetails.getUsername());
//        userInfoDTO.setIat(System.currentTimeMillis() / 1000L);
//        return ResponseEntity.ok(userInfoDTO);
//    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .statusCode(401)
                    .message("Unauthorized")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        String email = userDetails.getUsername();
        UserResponseDTO userResponseDTO = userService.getUserInfoByEmail(email);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setFirstName(userResponseDTO.getFirstName());
        userInfoDTO.setLastName(userResponseDTO.getLastName());
        userInfoDTO.setRole(userResponseDTO.getRole().toString());
        userInfoDTO.setId(userResponseDTO.getId());
        userInfoDTO.setActive(userResponseDTO.getIsActive());
        userInfoDTO.setEmail(userResponseDTO.getEmail());
        userInfoDTO.setSub(userDetails.getUsername());
        userInfoDTO.setIat(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));

        ApiResponse response = ApiResponse.builder()
                .success(true)
                .statusCode(200)
                .data(userInfoDTO)
                .message("User profile retrieved successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("me/roles")
    public ResponseEntity<ApiResponse> getCurrentUserRoles(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .statusCode(401)
                    .message("Unauthorized")
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setStatusCode(200);
        response.setTimestamp(LocalDateTime.now());

        String email = userDetails.getUsername();
        UserResponseDTO userResponseDTO = userService.getUserInfoByEmail(email);

        Role role = userResponseDTO.getRole();
        if (role == Role.STUDENT) {
            StudentResponseDTO studentResponseDTO = userService.getStudentById(userResponseDTO.getId());
            response.setData(studentResponseDTO);
            response.setMessage("Student profile retrieved successfully");
        } else if (role == Role.TUTOR) {
            TutorResponseDTO tutorResponseDTO = userService.getTutorById(userResponseDTO.getId());
            response.setData(tutorResponseDTO);
            response.setMessage("Tutor profile retrieved successfully");
        } else {
            response.setSuccess(false);
            response.setMessage("User role not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


        return ResponseEntity.ok(response);
    }

//    @GetMapping("{idStudent}")
//    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long idStudent) {
//        try {
//            StudentResponseDTO studentResponseDTO = userService.getStudentById(idStudent);
//            ApiResponse response = ApiResponse.builder()
//                    .success(true)
//                    .statusCode(200)
//                    .data(studentResponseDTO)
//                    .message("Student profile retrieved successfully")
//                    .timestamp(LocalDateTime.now())
//                    .build();
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            ApiResponse response = ApiResponse.builder()
//                    .success(false)
//                    .statusCode(401)
//                    .message("Error: " + e.getMessage())
//                    .timestamp(LocalDateTime.now())
//                    .build();
//            return ResponseEntity.status(200).body(response);
//        }
//    }
}
