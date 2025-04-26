package com.scaffold.template.repositories;

import com.scaffold.template.entities.InterestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<InterestEntity, Long> {
    List<InterestEntity> findByIsActiveTrue();

    //Mostrar todos los intereses cargados
    List<InterestEntity> findAll();

    List<InterestEntity> findByIsActiveFalse();

    InterestEntity findByName(String name);

    InterestEntity findByIdAndIsActiveTrue(Long id);
}
