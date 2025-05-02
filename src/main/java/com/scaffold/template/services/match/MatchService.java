package com.scaffold.template.services.match;

import com.scaffold.template.dtos.match.ConfirmedMatchResponseDTO;
import com.scaffold.template.dtos.match.MatchResponseDTO;
import com.scaffold.template.dtos.match.StudentPendingRequestMatchDTO;
import com.scaffold.template.dtos.profile.UserResponseDTO;
import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.Status;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que define las operaciones relacionadas con los matches entre estudiantes y tutores.
 */
@Service
public interface MatchService {

    /**
     * Obtiene los IDs de los tutores que tienen intereses en común con un usuario dado.
     *
     * @param userId ID del usuario para el cual se buscan tutores compatibles.
     * @return Lista de IDs de tutores con intereses en común.
     */
    List<Long> getIDsOfTutorsWithCommonInterests(Long userId);

    /**
     * Obtiene un tutor compatible con un estudiante dado.
     *
     * @param studentId ID del estudiante para el cual se busca un tutor compatible.
     * @return DTO con la información del tutor compatible.
     */
    UserResponseDTO getTutorCompatibleWithStudent(Long studentId);

    /**
     * Crea un nuevo match entre un estudiante y un tutor.
     *
     * @param studentId ID del estudiante.
     * @param tutorId ID del tutor.
     * @param isAccepted Indica si el match es aceptado inicialmente.
     * @return La entidad del match creado.
     */
    MatchEntity createMatch(Long studentId, Long tutorId, boolean isAccepted);

    /**
     * Convierte una entidad de match en un DTO de respuesta.
     *
     * @param matchEntity Entidad del match a convertir.
     * @return DTO con la información del match.
     */
    MatchResponseDTO mapToMatchResponseDTO(MatchEntity matchEntity);

    /**
     * Obtiene una lista de matches filtrados por estado y el ID del tutor.
     *
     * @param status Estado del match (por ejemplo, PENDING, ACCEPTED).
     * @param tutorId ID del tutor asociado a los matches.
     * @return Lista de entidades de matches que cumplen con los criterios.
     */
    List<MatchEntity> getMatchesByStatusAndTutorId(Status status, Long tutorId);

    /**
     * Convierte una lista de entidades de matches en una lista de DTOs
     * para solicitudes pendientes de estudiantes.
     *
     * @param matches Lista de entidades de matches a convertir.
     * @return Lista de DTOs con la información de las solicitudes pendientes.
     */
    List<StudentPendingRequestMatchDTO> mapToStudentPendingRequestMatchDTOs(List<MatchEntity> matches);

    /**
     * Acepta o rechaza un match existente.
     *
     * @param matchId ID del match a actualizar.
     * @param isAccepted Indica si el match es aceptado o rechazado.
     * @return La entidad del match actualizada.
     */
    MatchEntity acceptMatch(Long matchId, boolean isAccepted);

    /**
     * Obtiene una lista de matches filtrados por estado y el ID del usuario.
     *
     * @param status Estado del match (por ejemplo, PENDING, ACCEPTED).
     * @param userId ID del usuario asociado a los matches.
     * @return Lista de entidades de matches que cumplen con los criterios.
     */
    List<MatchEntity> getMatchesByStatusAndUserId(Status status, Long userId);

    ConfirmedMatchResponseDTO mapToConfirmedMatchResponseDTO(MatchEntity matchEntity, Long userId);//El userId
    // corresponde al usuario que solicita los chats.
}