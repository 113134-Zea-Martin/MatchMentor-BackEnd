package com.scaffold.template.repositories;

import com.scaffold.template.entities.MercadoPagoAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoPagoAuthRepository extends JpaRepository<MercadoPagoAuthEntity, Long> {
    MercadoPagoAuthEntity findByUserId(Long userId);
}
