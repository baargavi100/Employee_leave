package com.lms.controller;

import com.lms.dto.response.LeaveBalanceResponse;
import com.lms.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Year;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveBalanceController {

    private final LeaveBalanceService balanceService;

    /**
     * GET /api/leaves/balance/{employeeId}?year=2025
     * Get complete leave balance with comp-off breakdown
     */
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<LeaveBalanceResponse> getBalance(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year) {

        int targetYear = (year != null) ? year : Year.now().getValue();
        LeaveBalanceResponse response = balanceService.getBalance(employeeId, targetYear);

        return ResponseEntity.ok(response);
    }
}

