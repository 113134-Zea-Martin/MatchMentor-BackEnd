package com.scaffold.template.repositories;

import com.scaffold.template.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByMatchId(Long matchId);
    List<ChatEntity> findByMatchIdAndSenderIdAndIsReadFalse(Long matchId, Long senderId);
    Boolean existsByMatchIdAndSenderIdAndIsReadFalse(Long matchId, Long senderId);
}
