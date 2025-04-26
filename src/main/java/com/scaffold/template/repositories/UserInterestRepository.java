package com.scaffold.template.repositories;

import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.entities.UserInterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterestEntity, Long> {
    List<UserInterestEntity> findByUserId(Long userId);
    void deleteByUserIdAndInterestId(Long userId, Long interestId);

    @Query("SELECT u FROM UserEntity u JOIN UserInterestEntity ui ON u.id = ui.user.id WHERE ui.interest.id IN :interestIds")
    List<UserEntity> findByInterestIsIn(List<Long> interestIds);
}
