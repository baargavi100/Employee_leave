package com.lms.controller;

import com.lms.dto.response.LeaveBalanceResponse;
import com.lms.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Year;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class LeaveBalanceController {

    private final LeaveBalanceService balanceService;

    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<LeaveBalanceResponse> getBalance(
            @PathVariable Long employeeId,
            @RequestParam(required = false) Integer year) {

        log.info("[API] GET balance: employeeId={}, year={}", employeeId, year);

        int targetYear = (year != null) ? year : Year.now().getValue();
        LeaveBalanceResponse response = balanceService.getBalance(employeeId, targetYear);

        return ResponseEntity.ok(response);
    }
}

