package com.scaffold.template.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meeting;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "date", nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    public enum PaymentStatus {
//        pending: El usuario aún no ha completado el proceso de pago (por ejemplo, después de generar un boleto, el pago se completará cuando el usuario pague en el lugar seleccionado).
//        approved: El pago ha sido aprobado y acreditado con éxito.
//                authorized: El pago ha sido autorizado pero aún no se ha capturado.
//                in_process: El pago está en proceso de revisión.
//                in_mediation: El usuario ha iniciado una disputa.
//        rejected: El pago fue rechazado (el usuario puede intentar pagar nuevamente).
//        cancelled: El pago fue cancelado por alguna de las partes o caducó.
//                refunded: El pago fue reembolsado al usuario.
//        charged_back: Se realizó un contracargo en la tarjeta de crédito del comprador.
        PENDING,
        APPROVED,
        AUTHORIZED,
        IN_PROCESS,
        IN_MEDIATION,
        REJECTED,
        CANCELLED,
        REFUNDED,
        CHARGED_BACK
    }
}
