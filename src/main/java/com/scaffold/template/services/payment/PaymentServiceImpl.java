package com.scaffold.template.services.payment;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.scaffold.template.dtos.MercadoPagoNotificationDTO;
import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.PaymentEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.PaymentRepository;
import com.scaffold.template.services.meeting.MeetingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${frontend.url}")
    private String frontUrl;

    @Value("${backend.url}")
    private String backUrl;

    private double commission = 0.10; // 10% de comisión

    @Value("${mercadopago.access.token}")
    private String accessToken;

    private final PaymentRepository paymentRepository;
    private final MeetingService meetingService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              MeetingService meetingService) {
        this.paymentRepository = paymentRepository;
        this.meetingService = meetingService;
    }

    @Override
    public String createMeetingPayment(Long meetingId) throws MPException, MPApiException {

        MeetingEntity meeting = meetingService.getMeetingById(meetingId);

        UserEntity mentor = meeting.getMentor();
        String accessToken = mentor.getMercadoPagoToken();

        // Agrega credenciales de Mercado Pago
        MercadoPagoConfig.setAccessToken(accessToken);

        BigDecimal totalPrice = BigDecimal.valueOf(meeting.getHourlyRate())
                .multiply(BigDecimal.valueOf(meeting.getDuration()));

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(meeting.getId().toString())
                .title("Reunión con tutor")
                .description("Motivo: " + meeting.getReason())
                .categoryId("Servicios")
                .quantity(1)
                .currencyId("ARS") // o "ARS"
                .unitPrice(totalPrice)
                .build();

        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(frontUrl + "/meet")
                .pending(frontUrl + "/pending")
                .failure(frontUrl + "/failure")
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(List.of(itemRequest))
                .marketplaceFee(BigDecimal.valueOf(commission).multiply(totalPrice)) // 10% de comisión
                .externalReference(meeting.getId().toString())
                .notificationUrl(backUrl + "/api/payment/mercadopago/notification")
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);
        return preference.getInitPoint();
    }

    @Override
    public void createPayment(MercadoPagoNotificationDTO notification) {

        MercadoPagoConfig.setAccessToken(accessToken);

        String paymentId = notification.getData().getId();

        PaymentClient paymentClient = new PaymentClient();

        try {
            Payment payment = paymentClient.get(Long.parseLong(paymentId));

            if (notification.getType().equals("payment")) {
                PaymentEntity paymentEntity = new PaymentEntity();

                MeetingEntity meeting = meetingService.getMeetingById(Long.valueOf(payment.getExternalReference()));
                paymentEntity.setMeeting(meeting);
                paymentEntity.setTransactionId(paymentId);
                paymentEntity.setPaymentMethod(payment.getPaymentTypeId());
                paymentEntity.setAmount(payment.getTransactionAmount());
                paymentEntity.setStatus(PaymentEntity.PaymentStatus.valueOf(payment.getStatus().toUpperCase()));
                paymentEntity.setDate(payment.getDateCreated().toLocalDateTime().plusHours(1));

                paymentRepository.save(paymentEntity);
            }
        } catch (MPApiException | MPException e) {
            e.printStackTrace();
        }
    }
}
