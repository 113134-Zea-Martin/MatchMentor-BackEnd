package com.scaffold.template.services.payment;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.scaffold.template.dtos.MercadoPagoNotificationDTO;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    String createMeetingPayment(Long meetingId) throws MPException, MPApiException;
    void createPayment(MercadoPagoNotificationDTO notification);
}
