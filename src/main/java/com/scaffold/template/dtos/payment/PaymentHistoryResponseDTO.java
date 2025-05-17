package com.scaffold.template.dtos.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaffold.template.entities.PaymentEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentHistoryResponseDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private PaymentEntity.PaymentStatus paymentStatus;
    private String transactionType;
    private String counterpartName;
    private String description;
    private String paymentMethod;
    private String transactionId;
    private BigDecimal mercadoPagoFee;
    private BigDecimal platformFee;
}
