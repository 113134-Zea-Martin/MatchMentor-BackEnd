package com.scaffold.template.services.mappers;

import com.scaffold.template.dtos.UserRegisterRequestDTO;
import com.scaffold.template.dtos.UserRegisterResponseDTO;
import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Clase de utilidad para mapear entre entidades de usuario y DTOs relacionados.
 * Proporciona métodos estáticos para realizar las conversiones necesarias.
 */
@Component
public class UserMapper {

    /**
     * Convierte un DTO de registro de usuario en una entidad de usuario.
     *
     * @param userRegisterRequestDTO el DTO que contiene los datos del usuario a registrar
     * @return una instancia de {@link UserEntity} con los datos mapeados desde el DTO
     */
    public static UserEntity toEntity(UserRegisterRequestDTO userRegisterRequestDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userRegisterRequestDTO.getFirstName());
        userEntity.setLastName(userRegisterRequestDTO.getLastName());
        userEntity.setEmail(userRegisterRequestDTO.getEmail());
        userEntity.setPassword(userRegisterRequestDTO.getPassword());
        userEntity.setInterests(userRegisterRequestDTO.getInterests());
        return userEntity;
    }

    /**
     * Convierte una entidad de usuario en un DTO de respuesta de registro.
     *
     * @param userEntity la entidad de usuario que se desea convertir
     * @return una instancia de {@link UserRegisterResponseDTO} con los datos mapeados desde la entidad
     */
    public static UserRegisterResponseDTO toResponseDTO(UserEntity userEntity) {
        UserRegisterResponseDTO responseDTO = new UserRegisterResponseDTO();
        responseDTO.setFirstName(userEntity.getFirstName());
        responseDTO.setLastName(userEntity.getLastName());
        responseDTO.setEmail(userEntity.getEmail());
        responseDTO.setInterests(userEntity.getInterests());
        return responseDTO;
    }
}
