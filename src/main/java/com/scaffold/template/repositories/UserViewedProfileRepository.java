package com.scaffold.template.repositories;

import com.scaffold.template.entities.UserViewedProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserViewedProfileRepository extends JpaRepository<UserViewedProfileEntity, Long> {
    void deleteByViewerId(Long viewerId);
}
