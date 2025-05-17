package com.scaffold.template.services.payment;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.scaffold.template.dtos.MercadoPagoNotificationDTO;
import com.scaffold.template.dtos.payment.PaymentHistoryResponseDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    String createMeetingPayment(Long meetingId) throws MPException, MPApiException;
    void createPayment(MercadoPagoNotificationDTO notification);

    List<PaymentHistoryResponseDTO> getPaymentHistory(Long userId);
}
