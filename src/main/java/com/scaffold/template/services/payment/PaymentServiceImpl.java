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
import com.scaffold.template.dtos.payment.PaymentHistoryResponseDTO;
import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.PaymentEntity;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.PaymentRepository;
import com.scaffold.template.services.meeting.MeetingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                paymentEntity.setMercadoPagoFee(payment.getFeeDetails().get(0).getAmount());
                paymentEntity.setPlatformFee(payment.getFeeDetails().get(1).getAmount());
                paymentEntity.setStatus(PaymentEntity.PaymentStatus.valueOf(payment.getStatus().toUpperCase()));
                paymentEntity.setDate(payment.getDateCreated().toLocalDateTime().plusHours(1));

                paymentRepository.save(paymentEntity);

                // Actualiza el estado de la reunión
                if (paymentEntity.getStatus().equals(PaymentEntity.PaymentStatus.APPROVED)) {
                    meetingService.respondToMeeting(meeting.getId(), true);
                }

            }
        } catch (MPApiException | MPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PaymentHistoryResponseDTO> getPaymentHistory(Long userId) {
        // Buscar las reuniones del usuario
        List<MeetingEntity> meetings = meetingService.getMeetingHistory(userId);
        List<Long> meetingIds = new ArrayList<>();
        for (MeetingEntity meeting : meetings) {
            meetingIds.add(meeting.getId());
        }
        List<PaymentHistoryResponseDTO> paymentHistoryResponseDTOList = new ArrayList<>();
        for (Long meetingId : meetingIds) {
            List<PaymentEntity> payments = paymentRepository.findAllByMeetingId(meetingId);
            for (PaymentEntity payment : payments) {
                PaymentHistoryResponseDTO response = mapToPaymentHistoryResponseDTO(payment, userId);
                paymentHistoryResponseDTOList.add(response);
            }
        }
        return paymentHistoryResponseDTOList;
    }

    public PaymentHistoryResponseDTO mapToPaymentHistoryResponseDTO(PaymentEntity payment, Long userId) {
        PaymentHistoryResponseDTO response = new PaymentHistoryResponseDTO();
        response.setId(payment.getId());
        response.setDateTime(payment.getDate());
        response.setPaymentStatus(payment.getStatus());

        if (payment.getMeeting().getMentor() == null) {
            response.setTransactionType("Cobro");
            response.setCounterpartName(payment.getMeeting().getStudent().getFirstName() + " " + payment.getMeeting().getStudent().getLastName());
            response.setMercadoPagoFee(payment.getMercadoPagoFee());
            response.setPlatformFee(payment.getPlatformFee());
        } else {
            response.setTransactionType("Pago");
            response.setCounterpartName(payment.getMeeting().getMentor().getFirstName() + " " + payment.getMeeting().getMentor().getLastName());
            response.setMercadoPagoFee(BigDecimal.valueOf(0));
            response.setPlatformFee(BigDecimal.valueOf(0));
        }

        BigDecimal totalAmount = payment.getAmount().subtract(response.getMercadoPagoFee()).subtract(response.getPlatformFee());
        response.setAmount(totalAmount);

        response.setDescription(payment.getMeeting().getReason());
        response.setPaymentMethod(Objects.equals(payment.getPaymentMethod(), "account_money") ? "Dinero en cuenta" : "Tarjeta de crédito");
        response.setTransactionId(payment.getTransactionId());

        return response;
    }

}
