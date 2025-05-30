package com.scaffold.template.services.adminReport;

import com.scaffold.template.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public interface AdminReportService {
    Map<String, Object> getRegisteredUsersReport(String interval, LocalDate startDate, LocalDate endDate, String role);

//    Map<String, Object> getActiveUsersReport(LocalDate startDate, LocalDate endDate);

    // Método para otener el reporte de matchs con el intervalo de fechas
    Map<String, Integer> getMatchesReport(LocalDate startDate, LocalDate endDate);

    Map<String, Double> getMeetingsReport(LocalDate startDate, LocalDate endDate);
    Map<String, BigDecimal> getPaymentsReport(LocalDate startDate, LocalDate endDate);
    Map<String, BigDecimal> getTop3StudentsByPaymentFee(LocalDate startDate, LocalDate endDate);
    // Método para obtener el reporte de cantidad de pagos por estado y fechas, y cantidad de pagos totales por estado y fechas
    Map<String, BigDecimal> getPaymentsByStatusAndDate(LocalDate startDate, LocalDate endDate);

    Map<String, BigDecimal> getTop3TutorsByMatchesAccepted(LocalDate startDate, LocalDate endDate);

    Map<String, BigDecimal> getTopInterestByMatchAccepted(LocalDate startDate, LocalDate endDate);
}