package com.scaffold.template.repositories;

import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllByMeetingId(Long meetingId);
}
