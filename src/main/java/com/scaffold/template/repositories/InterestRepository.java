package com.scaffold.template.repositories;

import com.scaffold.template.entities.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<InterestEntity, Long> {
}
