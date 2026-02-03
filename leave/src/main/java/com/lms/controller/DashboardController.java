package com.lms.controller;

import com.lms.dto.response.*;
import com.lms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * ================================================================
     * GET /api/dashboard/monthly-stats/{employeeId}?year=2025&month=2
     * Get monthly statistics for EMPLOYEE dashboard
     * ================================================================
     */
    @GetMapping("/monthly-stats/{employeeId}")
    public ResponseEntity<MonthlyStatsResponse> getMonthlyStats(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

        log.info("[API] GET monthly-stats: employee={}, month={}/{}",
                employeeId, targetMonth, targetYear);

        MonthlyStatsResponse response = dashboardService.getMonthlyStats(
                employeeId, targetYear, targetMonth);

        return ResponseEntity.ok(response);
    }

    /**
     * ================================================================
     * GET /api/dashboard/team-balances/{managerId}?year=2025
     * Get all team members' balances for MANAGER dashboard
     * ================================================================
     */
    @GetMapping("/team-balances/{managerId}")
    public ResponseEntity<List<TeamMemberBalance>> getTeamBalances(
            @PathVariable Long managerId,
            @RequestParam(required = false) Integer year) {

        int targetYear = (year != null) ? year : Year.now().getValue();

        log.info("[API] GET team-balances: manager={}, year={}", managerId, targetYear);

        List<TeamMemberBalance> response = dashboardService.getTeamBalances(managerId, targetYear);

        return ResponseEntity.ok(response);
    }

    /**
     * ================================================================
     * GET /api/dashboard/pending-count/{managerId}
     * Get pending approvals count for MANAGER dashboard
     * ================================================================
     */
    @GetMapping("/pending-count/{managerId}")
    public ResponseEntity<Integer> getPendingCount(@PathVariable Long managerId) {
        log.info("[API] GET pending-count: manager={}", managerId);

        int count = dashboardService.getPendingCount(managerId);

        return ResponseEntity.ok(count);
    }
}
