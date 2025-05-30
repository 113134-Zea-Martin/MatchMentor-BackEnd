package com.scaffold.template.controllers;

import com.scaffold.template.services.adminReport.AdminReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/report")
public class AdminReportController {

    private final AdminReportService adminReportService;

    @Autowired
    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getRegisteredUsersReport(
            @RequestParam String interval, // DAY, WEEK, MONTH
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String role
    ) {
        Map<String, Object> reportData = adminReportService.getRegisteredUsersReport(interval, startDate, endDate, role);
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/matches")
    public ResponseEntity<Map<String, Integer>> getMatchesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Integer> reportData = adminReportService.getMatchesReport(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/meetings")
    public ResponseEntity<Map<String, Double>> getMeetingsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, Double> reportData = adminReportService.getMeetingsReport(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/payments")
    public ResponseEntity<Map<String, BigDecimal>> getPaymentsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, BigDecimal> reportData = adminReportService.getPaymentsReport(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/top-students")
    public ResponseEntity<Map<String, BigDecimal>> getTop3StudentsByPaymentFee(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, BigDecimal> reportData = adminReportService.getTop3StudentsByPaymentFee(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/top-tutors")
    public ResponseEntity<Map<String, BigDecimal>> getTop3TutorsByMatchesAccepted(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, BigDecimal> reportData = adminReportService.getTop3TutorsByMatchesAccepted(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/top-interests")
    public ResponseEntity<Map<String, BigDecimal>> getTop3InterestsByMatchesAccepted(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, BigDecimal> reportData = adminReportService.getTopInterestByMatchAccepted(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }


    @GetMapping("/payments-by-status")
    public ResponseEntity<Map<String, BigDecimal>> getPaymentsByStatusAndDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Map<String, BigDecimal> reportData = adminReportService.getPaymentsByStatusAndDate(startDate, endDate);
        return ResponseEntity.ok(reportData);
    }

}
