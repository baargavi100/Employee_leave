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

    private final LeaveBalanceService leaveBalanceService;

    /**
     * GET /api/leaves/balance/{employeeId}?year=2025
     */
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<LeaveBalanceResponse> getLeaveBalance(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year
    ) {
        int targetYear = (year != null) ? year : Year.now().getValue();
        LeaveBalanceResponse response =
                leaveBalanceService.getBalance(employeeId, targetYear);

        return ResponseEntity.ok(response);
    }
}
