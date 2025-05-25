package com.scaffold.template.services.adminReport;

import com.scaffold.template.entities.MatchEntity;
import com.scaffold.template.entities.MeetingEntity;
import com.scaffold.template.entities.PaymentEntity;
import com.scaffold.template.entities.Role;
import com.scaffold.template.entities.Status;
import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.MatchRepository;
import com.scaffold.template.repositories.MeetingRepository;
import com.scaffold.template.repositories.PaymentRepository;
import com.scaffold.template.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminReportServiceImpl implements AdminReportService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MeetingRepository meetingRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public AdminReportServiceImpl(UserRepository userRepository,
                                  MatchRepository matchRepository,
                                  MeetingRepository meetingRepository,
                                  PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.meetingRepository = meetingRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Map<String, Object> getRegisteredUsersReport(String interval, LocalDate startDate, LocalDate endDate, String role) {

        // Obtener usuarios dentro del rango de fechas y rol
        List<UserEntity> filteredUsers;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        String growth = "growth";
        long initialUsersCount,
                finalUsersCount;
        BigDecimal growthPercentage;

        if (role != null && !role.isEmpty()) {
            filteredUsers = userRepository.findByCreatedAtBetweenAndRole(
                    startDateTime,
                    endDateTime,
                    Role.valueOf(role.toUpperCase())
            );
            initialUsersCount = userRepository.countByRoleAndCreatedAtBefore(Role.valueOf(role.toUpperCase()), startDateTime);
            finalUsersCount = userRepository.countByRoleAndCreatedAtBefore(Role.valueOf(role.toUpperCase()), endDateTime);
        } else {
            filteredUsers = userRepository.findStudentsAndTutorsByCreatedAtBetween(
                    startDateTime,
                    endDateTime
            );
            initialUsersCount = userRepository.countByRoleNotAndCreatedAtBefore(Role.ADMIN, startDateTime);
            finalUsersCount = userRepository.countByRoleNotAndCreatedAtBefore(Role.ADMIN, endDateTime);
        }

        // Calcular el porcentaje de crecimiento
        if (initialUsersCount > 0) {
            growthPercentage = BigDecimal.valueOf(finalUsersCount - initialUsersCount)
                    .divide(BigDecimal.valueOf(initialUsersCount))
                    .multiply(BigDecimal.valueOf(100));
        } else {
            growthPercentage = BigDecimal.ZERO;
        }


//        long totalUsers = userRepository.count(); // Total acumulado de todos los usuarios
        long totalUsers = filteredUsers.size(); // Total acumulado de todos los usuarios

        // Mapear y contar usuarios por el intervalo definido
        Map<String, Long> rawCounts = new HashMap<>();
        DateTimeFormatter formatter;

        switch (interval.toUpperCase()) {
            case "DAY":
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                break;
//            case "WEEK":
//                // Usar WeekFields para obtener el número de semana local
//                WeekFields weekFields = WeekFields.of(Locale.getDefault()); // O un Locale específico como es_ES
//                // Para establecer explícitamente el lunes como primer día de la semana:
////                WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
////                formatter = DateTimeFormatter.ofPattern("yyyy-'W'ww"); // Ejemplo: 2024-W20
//                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                break;
            case "MONTH":
            default:
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                break;
        }

        for (UserEntity user : filteredUsers) {
            String period = user.getCreatedAt().format(formatter);
            rawCounts.put(period, rawCounts.getOrDefault(period, 0L) + 1);
        }

        // Rellenar periodos vacíos con 0s y ordenar
        Map<String, Long> monthlyEvolution = fillMissingPeriodsAndSort(rawCounts, interval.toUpperCase(), startDate, endDate, formatter);


        Map<String, Object> reportData = new HashMap<>();
        reportData.put("totalUsers", totalUsers);
        reportData.put("evolutionData", monthlyEvolution.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("period", entry.getKey());
                    item.put("count", entry.getValue());
                    return item;
                })
                .collect(Collectors.toList()));

        reportData.put(growth, growthPercentage);

        return reportData;
    }

    @Override
    public Map<String, Integer> getMatchesReport(LocalDate startDate, LocalDate endDate) {
        // Transformar las fechas de inicio y fin a LocalDateTime
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);


        // Contar matches creados dentro del rango de fechas
        Integer totalMatches = matchRepository.countByCreatedAtBetween(
                startDateTime,
                endDateTime
        );

        // Contar matches pendientes dentro del rango de fechas
        Integer totalPendingMatches = matchRepository.countByCreatedAtBetweenAndStatus(
                startDateTime,
                endDateTime,
                Status.PENDING
        );

        // Contar matches aceptados dentro del rango de fechas
        Integer totalAcceptedMatches = matchRepository.countByCreatedAtBetweenAndStatus(
                startDateTime,
                endDateTime,
                Status.ACCEPTED
        );

        // Contar matches rechazados dentro del rango de fechas
        Integer totalRejectedMatches = matchRepository.countByCreatedAtBetweenAndStatus(
                startDateTime,
                endDateTime,
                Status.REJECTED
        );

        // Mapear y contar matches por el intervalo definido
        Map<String, Integer> reportData = new HashMap<>();
        String matchesTotales = "matchesTotales";
        String matchesPendientes = "matchesPendientes";
        String matchesAceptados = "matchesAceptados";
        String matchesRechazados = "matchesRechazados";

        reportData.put(matchesTotales, totalMatches);
        reportData.put(matchesPendientes, totalPendingMatches);
        reportData.put(matchesAceptados, totalAcceptedMatches);
        reportData.put(matchesRechazados, totalRejectedMatches);

        return reportData;

    }

    // Método para obtener el reporte de reuniones con el intervalo de fechas
    // Este reporte nos indicará el porcentaje de reuniones que se han realizado en comparación con el total de matches,
    // mostrando la información dia por dia.
    @Override
    public Map<String, Double> getMeetingsReport(LocalDate startDate, LocalDate endDate) {
        // Contar matches creados dentro del rango de fechas dia por dia
        Map<String, Double> dailyMeetingsCount = new LinkedHashMap<>(); // Mantiene el orden de inserción
        LocalDate current = startDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (!current.isAfter(endDate)) {
            List<MatchEntity> matches = matchRepository.findByCreatedAtBetweenAndStatus(current.atStartOfDay(), current.atTime(LocalTime.MAX), Status.ACCEPTED);
            int totalMatches = matches.size();

            int totalMeetings = 0;
            for (MatchEntity match : matches) {
                if (meetingRepository.existsByMatchIdAndStatus(match.getId(), MeetingEntity.MeetingStatus.ACCEPTED)) {
                    totalMeetings++;
                }
            }
            // Calcular el porcentaje de reuniones realizadas
            Double percentage =  (totalMatches > 0 ?  ((double) totalMeetings / totalMatches * 100) : 0);
//            Double percentage = (double) totalMeetings;
            String period = current.format(formatter);
            dailyMeetingsCount.put(period, percentage);
            current = current.plusDays(1); // Avanzar al siguiente día
        }
        // Retornar el reporte de reuniones
        return dailyMeetingsCount;
    }

    public Map<String, Long> fillMissingPeriodsAndSort(Map<String, Long> rawCounts, String interval, LocalDate startDate, LocalDate endDate, DateTimeFormatter formatter) {
        Map<String, Long> filledCounts = new LinkedHashMap<>(); // Mantiene el orden de inserción
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            String period;
            switch (interval) {
                case "DAY":
                    period = current.format(formatter);
                    filledCounts.put(period, rawCounts.getOrDefault(period, 0L));
                    current = current.plusDays(1);
                    break;
                case "WEEK":
                    period = current.format(formatter);
                    filledCounts.put(period, rawCounts.getOrDefault(period, 0L));
                    current = current.plusWeeks(1);
                    break;
                case "MONTH":
                default:
                    period = current.format(formatter);
                    filledCounts.put(period, rawCounts.getOrDefault(period, 0L));
                    current = current.plusMonths(1);
                    // Asegurarse de ir al primer día del siguiente mes para evitar saltos.
                    current = current.with(TemporalAdjusters.firstDayOfMonth());
                    break;
            }
        }
        return filledCounts;
    }

    @Override
    public Map<String, BigDecimal> getPaymentsReport(LocalDate startDate, LocalDate endDate) {

        Map<String, BigDecimal> paymentsCount = new LinkedHashMap<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime startDateTime = date.atStartOfDay();
            LocalDateTime endDateTime = date.atTime(LocalTime.MAX);
            String period = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            BigDecimal count;
            count = paymentRepository.findTotalByStatusAndDateBetween(
                    PaymentEntity.PaymentStatus.APPROVED,
                    startDateTime,
                    endDateTime
            ) != null ? paymentRepository.findTotalByStatusAndDateBetween(
                    PaymentEntity.PaymentStatus.APPROVED,
                    startDateTime,
                    endDateTime
            ) : BigDecimal.ZERO;
            paymentsCount.put(period, count);
        }
        // Retornar el reporte de pagos
        return paymentsCount;
    }

    @Override
    public Map<String, BigDecimal> getTop3StudentsByPaymentFee(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> results = paymentRepository.findTop3StudentsByPaymentFee(startDateTime, endDateTime);

        Map<String, BigDecimal> topStudents = new LinkedHashMap<>();
        for (Object[] result : results) {
            String studentId = result[1].toString();
            Optional<UserEntity> user = userRepository.findById(Long.parseLong(studentId));
            String studentFullName = user.map(userEntity -> userEntity.getFirstName() + " " + userEntity.getLastName())
                    .orElse("Unknown Student");
            BigDecimal fee = (BigDecimal) result[0];
            topStudents.put(studentFullName, fee);
        }

        return topStudents;
    }

    @Override
    public Map<String, BigDecimal> getPaymentsByStatusAndDate(LocalDate startDate, LocalDate endDate) {
        Map<String, BigDecimal> paymentsByStatus = new LinkedHashMap<>();

        String totalApprovedPayments = "totalApprovedPayments";
        Integer totalApprovedCount = paymentRepository.countByStatusAndDateBetween(
                PaymentEntity.PaymentStatus.APPROVED,
                startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );
        if (totalApprovedCount == null) {
            totalApprovedCount = 0;
        }
        paymentsByStatus.put(totalApprovedPayments, BigDecimal.valueOf(totalApprovedCount));

        String totalApprovedSum = "totalApprovedSum";
        BigDecimal total = BigDecimal.ZERO;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime startDateTime = date.atStartOfDay();
            LocalDateTime endDateTime = date.atTime(LocalTime.MAX);
            BigDecimal count = paymentRepository.findTotalByStatusAndDateBetween(
                    PaymentEntity.PaymentStatus.APPROVED,
                    startDateTime,
                    endDateTime
            );
            if (count != null) {
                total = total.add(count);
            }
        }
        paymentsByStatus.put(totalApprovedPayments, BigDecimal.valueOf(totalApprovedCount));
        paymentsByStatus.put(totalApprovedSum, total);

        String averageApprovedPayment = "averageApprovedPayment";
        BigDecimal average = totalApprovedCount > 0 ? total.divide(BigDecimal.valueOf(totalApprovedCount), BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        paymentsByStatus.put(averageApprovedPayment, average);

        return paymentsByStatus;

    }

}