package com.scaffold.template.repositories;

import com.scaffold.template.entities.PasswordResetToken;
import com.scaffold.template.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    List<PasswordResetToken> findByUserId(Long userId);
    void deleteByUser(UserEntity user);
}
