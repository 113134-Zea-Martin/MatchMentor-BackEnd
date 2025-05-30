package com.scaffold.template.repositories;

import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllByMeetingId(Long meetingId);

    // Método para obtener el total de pagos segun el estado y las fechas
    @Query("SELECT SUM(p.platformFee) FROM PaymentEntity p WHERE p.status = :status AND p.date BETWEEN :startDate AND :endDate")
    BigDecimal findTotalByStatusAndDateBetween(PaymentEntity.PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);

//    SELECT SUM(p.platform_fee) as fee, m.student_id FROM payments p
//    JOIN meetings m on p.meeting_id = m.id
//    WHERE p.status = 'APPROVED'
//    and p.date between '2025-05-21' and '2025-05-23'
//    group by m.student_id
//    ORDER BY fee desc
//    limit 3;
    @Query("SELECT SUM(p.platformFee) AS fee, m.student.id FROM PaymentEntity p " +
           "JOIN p.meeting m " +
           "WHERE p.status = 'APPROVED' AND p.date BETWEEN :startDate AND :endDate " +
           "GROUP BY m.student.id " +
           "ORDER BY fee DESC limit 3")
    List<Object[]> findTop3StudentsByPaymentFee(LocalDateTime startDate, LocalDateTime endDate);

    // Método para obtener la cantidad de pagos aprobados y fechas
    @Query("SELECT COUNT(p) FROM PaymentEntity p WHERE p.status = :status AND p.date BETWEEN :startDate AND :endDate")
    Integer countByStatusAndDateBetween(PaymentEntity.PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);


    // Método para obtener la cantidad de pagos por estado y fechas
//    Integer countByStatusAndDateBetween(PaymentEntity.PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);
}