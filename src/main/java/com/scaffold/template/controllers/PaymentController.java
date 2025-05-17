package com.scaffold.template.controllers;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.scaffold.template.dtos.ApiResponse;
import com.scaffold.template.dtos.MercadoPagoNotificationDTO;
import com.scaffold.template.services.payment.PaymentService;
import com.scaffold.template.services.payment.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/mercadopago/{meetingId}")
    public String getMercadoPagoUrl(@PathVariable Long meetingId) throws MPException, MPApiException {
        return paymentService.createMeetingPayment(meetingId);
    }

    @PostMapping("/mercadopago/notification")
    public ResponseEntity notification(@RequestBody MercadoPagoNotificationDTO notification) {
        paymentService.createPayment(notification);
        return ResponseEntity.ok().build();
    }

    // userId puede ser studentId o mentorId
    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse> getPaymentHistory(@PathVariable Long userId) {
        ApiResponse response = new ApiResponse();
        response.setTimestamp(LocalDateTime.now());
        try {
            response.setSuccess(true);
            response.setStatusCode(200);
            response.setData(paymentService.getPaymentHistory(userId));
            response.setMessage("Historial de pagos obtenido correctamente");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setStatusCode(500);
            response.setMessage("Error al obtener el historial de pagos: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
