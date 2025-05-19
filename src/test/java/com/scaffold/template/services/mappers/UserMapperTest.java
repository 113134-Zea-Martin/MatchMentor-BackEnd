package com.scaffold.template.services.mappers;

import com.scaffold.template.dtos.auth.UserRegisterRequestDTO;
import com.scaffold.template.dtos.auth.UserRegisterResponseDTO;
import com.scaffold.template.entities.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    @Test
    void toEntityCorrectlyMapsBasicUserFields() {
        UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO();
        requestDTO.setFirstName("Ana");
        requestDTO.setLastName("García");
        requestDTO.setEmail("ana.garcia@example.com");
        requestDTO.setPassword("password123");

        UserEntity result = UserMapper.toEntity(requestDTO);

        assertEquals("Ana", result.getFirstName());
        assertEquals("García", result.getLastName());
        assertEquals("ana.garcia@example.com", result.getEmail());
        assertEquals("password123", result.getPassword());
        assertNotNull(result.getUserInterests());
        assertTrue(result.getUserInterests().isEmpty());
    }

    @Test
    void toResponseDTOCorrectlyMapsBasicUserFields() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Carlos");
        userEntity.setLastName("Rodríguez");
        userEntity.setEmail("carlos.rodriguez@example.com");

        UserRegisterResponseDTO result = UserMapper.toResponseDTO(userEntity);

        assertEquals("Carlos", result.getFirstName());
        assertEquals("Rodríguez", result.getLastName());
        assertEquals("carlos.rodriguez@example.com", result.getEmail());
        assertEquals("", result.getInterests());
    }

    @Test
    void toEntityHandlesNullValues() {
        UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO();
        requestDTO.setFirstName(null);
        requestDTO.setLastName(null);
        requestDTO.setEmail(null);
        requestDTO.setPassword(null);

        UserEntity result = UserMapper.toEntity(requestDTO);

        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
        assertNull(result.getPassword());
        assertNotNull(result.getUserInterests());
    }

    @Test
    void toResponseDTOHandlesNullValues() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(null);
        userEntity.setLastName(null);
        userEntity.setEmail(null);

        UserRegisterResponseDTO result = UserMapper.toResponseDTO(userEntity);

        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
        assertEquals("", result.getInterests());
    }

    @Test
    void toEntityCreatesNonNullUserInterestsList() {
        UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO();

        UserEntity result = UserMapper.toEntity(requestDTO);

        assertNotNull(result.getUserInterests());
        assertEquals(0, result.getUserInterests().size());
    }
}