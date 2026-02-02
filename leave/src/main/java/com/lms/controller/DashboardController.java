package com.lms.controller;

import com.lms.dto.response.*;
import com.lms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard/monthly-stats/{employeeId}?year=2025&month=2
     * Get monthly statistics for employee or manager dashboard
     */
    @GetMapping("/monthly-stats/{employeeId}")
    public ResponseEntity<MonthlyStatsResponse> getMonthlyStats(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

        MonthlyStatsResponse response = dashboardService.getMonthlyStats(
                employeeId, targetYear, targetMonth);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/dashboard/pending/{managerId}
     * Get pending approvals count for manager dashboard
     */
    @GetMapping("/pending/{managerId}")
    public ResponseEntity<PendingApprovalsResponse> getPendingApprovals(
            @PathVariable Long managerId) {

        PendingApprovalsResponse response = dashboardService.getPendingApprovals(managerId);
        return ResponseEntity.ok(response);
    }
}